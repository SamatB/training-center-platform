package com.training.notificationservice.service;

import com.training.notificationservice.dto.NotificationCreateRequest;
import com.training.notificationservice.dto.NotificationResponse;
import com.training.notificationservice.entity.Notification;
import com.training.notificationservice.mapper.NotificationMapper;
import com.training.notificationservice.repository.NotificationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationMapper notificationMapper;
    private final EmailService emailService;

    @Transactional
    public NotificationResponse create(NotificationCreateRequest request) {

        Notification notification = notificationMapper.toEntity(request);

        Notification saved = notificationRepository.save(notification);

        return notificationMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public NotificationResponse getById(UUID id) {

        Optional<Notification> notification = notificationRepository.findById(id);

        if (notification.isEmpty()) {
            throw new EntityNotFoundException(
                    "Запись не найдена с id: " + id
            );
        }

        Notification foundNotification = notification.get();

        return notificationMapper.toResponse(foundNotification);
    }
    public void sendEmail(String to,String subject,String message){
        emailService.sendEmail(to,subject,message);
    }
}