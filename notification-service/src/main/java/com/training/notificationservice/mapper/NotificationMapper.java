package com.training.notificationservice.mapper;

import com.training.notificationservice.dto.NotificationCreateRequest;
import com.training.notificationservice.dto.NotificationResponse;
import com.training.notificationservice.entity.Notification;
import com.training.notificationservice.entity.NotificationStatus;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class NotificationMapper {

    public Notification toEntity(NotificationCreateRequest request) {

        return Notification.builder()
                .userId(request.userId())
                .recipient(request.recipient())
                .subject(request.subject())
                .message(request.message())
                .type(request.type())
                .build();
    }

    public NotificationResponse toResponse(Notification notification) {

        return new NotificationResponse(
                notification.getId(),
                notification.getUserId(),
                notification.getRecipient(),
                notification.getSubject(),
                notification.getMessage(),
                notification.getType(),
                notification.getStatus(),
                notification.getCreatedAt(),
                notification.getSentAt()
        );
    }
}