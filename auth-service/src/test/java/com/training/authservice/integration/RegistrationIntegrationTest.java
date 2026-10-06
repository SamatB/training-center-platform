package com.training.authservice.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.authservice.dto.request.RegisterRequest;
import com.training.authservice.entity.UserAccount;
import com.training.authservice.repository.UserAccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
class RegistrationIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void cleanDatabase() {
        userAccountRepository.deleteAll();
    }

    @Test
    void shouldRegisterUser() throws Exception {

        RegisterRequest request = RegisterRequest.builder()
                .firstName("Samat")
                .lastName("Test")
                .email("samat.test@example.com")
                .password("password123")
                .build();

        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName").value("Samat"))
                .andExpect(jsonPath("$.lastName").value("Test"))
                .andExpect(jsonPath("$.email").value("samat.test@example.com"))
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andExpect(jsonPath("$.enabled").value(true));

        UserAccount savedUser = userAccountRepository
                .findByEmail("samat.test@example.com")
                .orElseThrow();

        assertThat(savedUser.getFirstName()).isEqualTo("Samat");
        assertThat(savedUser.getLastName()).isEqualTo("Test");
        assertThat(savedUser.getRole().name()).isEqualTo("STUDENT");
        assertThat(savedUser.getEnabled()).isTrue();

        assertThat(savedUser.getPassword())
                .isNotEqualTo("password123");

        assertThat(
                passwordEncoder.matches(
                        "password123",
                        savedUser.getPassword()
                )
        ).isTrue();
    }

    @Test
    void shouldNotRegisterUserWithDuplicateEmail() throws Exception {

        RegisterRequest request = RegisterRequest.builder()
                .firstName("Samat")
                .lastName("Test")
                .email("duplicate@example.com")
                .password("password123")
                .build();

        // Первая регистрация — успешная
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        // Вторая регистрация с тем же email
        mockMvc.perform(post("/api/auth/register")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict());

        // В БД всё равно должен быть только один пользователь
        long count = userAccountRepository.count();

        assertThat(count).isEqualTo(1);
    }
}