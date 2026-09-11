package com.training.authservice.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Запрос на обновление профиля текущего пользователя")
public record UpdateProfileRequest(
        @NotBlank(message = "Имя не должно быть пустым")
        @Size(min = 2, max = 100, message = "Имя должно содержать от 2 до 100 символов")
        @Schema(description = "Имя пользователя", example = "Иван")
        String firstName,

        @NotBlank(message = "Фамилия не должна быть пустой")
        @Size(min = 2, max = 100, message = "Фамилия должна содержать от 2 до 100 символов")
        @Schema(description = "Фамилия пользователя", example = "Иванов")
        String lastName,

        @NotBlank(message = "Электронная почта обязательна")
        @Email(message = "Электронная почта должна иметь корректный формат")
        @Size(max = 255, message = "Электронная почта не должна превышать 255 символов")
        @Schema(description = "Электронная почта пользователя", example = "ivan.ivanov@gmail.com")
        String email
) {
}
