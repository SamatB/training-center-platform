package com.training.notificationservice.kafka;

import com.training.notificationservice.client.UserClient;
import com.training.notificationservice.client.dto.UserResponse;
import com.training.notificationservice.event.EnrollmentCreatedEvent;
import com.training.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EnrollmentCreatedListener {

    private final UserClient userClient;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${app.kafka.topics.enrollment-created}",
            groupId = "notification-service"
    )
    public void handle(EnrollmentCreatedEvent event) {

        log.info("Получен EnrollmentCreatedEvent: enrollmentId={}, userId={}, courseId={}",
                event.enrollmentId(),
                event.userId(),
                event.courseId());

        UserResponse user = userClient.getUserById(event.userId());

        notificationService.sendEmail(
                user.email(),
                "Запись на курс",
                "Вы успешно записаны на курс. ID записи на курс: " + event.enrollmentId()
        );

        log.info("Уведомление о записи на курс отправлен на {}", user.email());
    }
}