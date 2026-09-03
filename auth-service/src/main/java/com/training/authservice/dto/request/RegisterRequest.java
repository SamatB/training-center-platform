package com.training.authservice.dto.request;

import com.training.authservice.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request for registering a new user")
public class RegisterRequest {

    @NotBlank(message = "Имя не должно быть пустым")
    @Size(
            min = 2,
            max = 100,
            message = "Имя должно содержать от 2 до 100 символов"
    )
    @Schema(
            description = "Имя пользователя",
            example = "John"
    )
    private String firstName;

    @NotBlank(message = "Фамилия не должна быть пустым")
    @Size(
            min = 2,
            max = 100,
            message = "Фамилия должна содержать от 2 до 100 символов"
    )
    @Schema(
            description = "Фамилия пользователя",
            example = "Doe"
    )
    private String lastName;

    @Email(message = "Электронная почта должна быть действительной")
    @NotBlank(message = "Требуется электронная почта")
    @Size(
            max = 255,
            message = "Электронный адрес не должен превышать 255 символов"
    )
    @Schema(
            description = "Адрес электронной почты пользователя",
            example = "john.doe@gmail.com"
    )
    private String email;

    @NotBlank(message = "Требуется пароль")
    @Size(
            min = 8,
            max = 100,
            message = "Пароль должен содержать от 8 до 100 символов"
    )
    @Schema(
            description = "Пароль пользователя",
            example = "password123",
            format = "password"
    )
    private String password;

    @NotNull(message = "Роль не должна быть null")
    @Schema(
            description = "Роль пользователя",
            example = "STUDENT"
    )
    private Role role;
}