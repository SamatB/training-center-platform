package com.training.notificationservice.dto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import com.training.notificationservice.entity.NotificationType;

import java.util.UUID;

public record NotificationCreateRequest(
        @NotNull(message = "User ID must not be null")
        UUID userId,

        @NotBlank(message = "Recipient must not be blank")
        @Email(message = "Recipient must be a valid email address")
        @Size(max = 255, message = "Recipient must not exceed 255 characters")
        String recipient,

        @NotBlank(message = "Subject must not be blank")
        @Size(max = 255, message = "Subject must not exceed 255 characters")
        String subject,

        @NotBlank(message = "Message must not be blank")
        String message,

        @NotNull(message = "Notification type must not be null")
        NotificationType type
) {
}