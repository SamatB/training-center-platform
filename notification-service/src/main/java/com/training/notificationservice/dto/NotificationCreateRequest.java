package com.training.notificationservice.dto;
import com.training.notificationservice.entity.NotificationType;

import java.util.UUID;

public record NotificationCreateRequest(
        UUID userId,
        String recipient,
        String subject,
        String message,
        NotificationType type
){

}