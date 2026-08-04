package com.training.notificationservice.repository;

import com.training.notificationservice.entity.Notification;
import com.training.notificationservice.entity.NotificationStatus;
import com.training.notificationservice.entity.NotificationType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotificationRepository extends JpaRepository <Notification, UUID> {

    List<Notification> findByUserId(UUID userId);

    List<Notification> findByStatus(NotificationStatus status);

    List<Notification> findByType(NotificationType type);

    List<Notification> findByUserIdAndStatus(UUID userId ,NotificationStatus status);

}