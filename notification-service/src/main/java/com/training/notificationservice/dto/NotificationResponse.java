package com.training.notificationservice.dto;
import com.training.notificationservice.entity.NotificationStatus;
import com.training.notificationservice.entity.NotificationType;

import java.time.LocalDateTime;
import java.util.UUID;


public record NotificationResponse(
        UUID id,
        UUID userId,
        String recipient,
        String subject,
        String message,
        NotificationType type,
        NotificationStatus status,
        LocalDateTime createdAt,
        LocalDateTime sentAt
) {
}
