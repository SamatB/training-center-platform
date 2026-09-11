package com.training.notificationservice.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.training.notificationservice.entity.NotificationType;

import java.util.UUID;

public record NotificationCreateRequest(
        @NotNull(message = "ID пользователя не должен быть пустым")
        UUID userId,

        @NotBlank(message = "Получатель не должен быть пустым")
        @Email(message = "Email получателя должен быть корректным")
        @Size(max = 255, message = "Email получателя не должен превышать 255 символов")
        String recipient,

        @NotBlank(message = "Тема сообщения не должна быть пустой")
        @Size(max = 255, message = "Тема сообщения не должна превышать 255 символов")
        String subject,

        @NotBlank(message = "Текст сообщения не должен быть пустым")
        String message,

        @NotNull(message = "Тип уведомления не должен быть пустым")
        NotificationType type
) {
}