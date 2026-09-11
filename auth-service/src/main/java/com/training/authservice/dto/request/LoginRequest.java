package com.training.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Запрос для аутентификации пользователя")
public record LoginRequest(

        @NotBlank(message = "Электронная почта не может быть пустой")
        @Email(message = "Некорректный формат электронной почты")
        @Schema(example = "student@example.com")
        String email,

        @NotBlank(message = "Пароль не может быть пустым")
        @Schema(example = "password123", format = "password")
        String password

) {
}
