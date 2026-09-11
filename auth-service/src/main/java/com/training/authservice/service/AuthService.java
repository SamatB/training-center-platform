package com.training.authservice.service;

import com.training.authservice.dto.request.LoginRequest;
import com.training.authservice.dto.request.UpdateProfileRequest;
import com.training.authservice.dto.response.AuthResponse;
import com.training.authservice.dto.response.ProfileUpdateResponse;
import com.training.authservice.dto.response.UserAccountResponse;
import com.training.authservice.entity.UserAccount;
import com.training.authservice.exception.EntityAlreadyExistsException;
import com.training.authservice.exception.EntityNotFoundException;
import com.training.authservice.mapper.UserAccountMapper;
import com.training.authservice.repository.UserAccountRepository;
import com.training.authservice.security.JwtService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserAccountRepository userAccountRepository;
    private final UserAccountMapper mapper;

    public AuthResponse login(@Valid LoginRequest  request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        UserAccount user = (UserAccount) authentication.getPrincipal();
        String token = jwtService.generateAccessToken(user);
        log.info("Пользователь успешно авторизован: userId={}", user.getId());
        return new AuthResponse(token, "Bearer");
    }

    @Transactional(readOnly = true)
    public UserAccountResponse getCurrentUser() {
        return mapper.toResponse(getCurrentUserAccount());
    }

    @Transactional
    public ProfileUpdateResponse updateCurrentUser(UpdateProfileRequest request) {
        UserAccount user = getCurrentUserAccount();

        if (userAccountRepository.existsByEmailAndIdNot(request.email(), user.getId())) {
            throw new EntityAlreadyExistsException(
                    String.format("Пользователь с электронной почтой '%s' уже существует", request.email())
            );
        }

        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setEmail(request.email());
        user.setUpdatedAt(LocalDateTime.now());

        UserAccount savedUser = userAccountRepository.save(user);
        String newAccessToken = jwtService.generateAccessToken(savedUser);

        log.info("Профиль пользователя успешно обновлён: userId={}", savedUser.getId());

        return new ProfileUpdateResponse(
                mapper.toResponse(savedUser),
                newAccessToken,
                "Bearer"
        );
    }

    @Transactional
    public void deleteCurrentUser() {
        UserAccount user = getCurrentUserAccount();
        userAccountRepository.delete(user);
        SecurityContextHolder.clearContext();
        log.info("Учётная запись пользователя удалена: userId={}", user.getId());
    }

    private UserAccount getCurrentUserAccount() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new EntityNotFoundException("Текущий пользователь не найден");
        }

        String email = authentication.getName();
        return userAccountRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Текущий пользователь не найден"));
    }
}
