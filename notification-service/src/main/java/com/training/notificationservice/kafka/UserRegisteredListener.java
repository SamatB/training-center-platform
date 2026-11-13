package com.training.notificationservice.kafka;

import com.training.notificationservice.client.UserClient;
import com.training.notificationservice.client.dto.UserResponse;
import com.training.notificationservice.event.UserRegisteredEvent;
import com.training.notificationservice.service.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserRegisteredListener {

    private final UserClient userClient;
    private final NotificationService notificationService;

    @KafkaListener(
            topics = "${app.kafka.topics.user-registered}",
            groupId = "notification-service-user-registered",
            properties = {
                    "spring.json.value.default.type=com.training.notificationservice.event.UserRegisteredEvent"
            }
    )
    public void handle(UserRegisteredEvent event) {

        log.info("Получено UserRegisteredEvent: userId={}, email={}", event.userId(), event.email());
        log.info("Получение профиля пользователя из user-service: userId={}", event.userId());

        UserResponse user = userClient.getUserById(event.userId());

        log.info("Профиль пользователя получен: userId={}, email={}", user.id(), user.email());

        String subject = "Добро пожаловать в Training Center Platform";
        String message = """
                Здравствуйте, %s!
                Вы успешно зарегистрировались в Training Center Platform.
                Желаем успешного обучения!
                """.formatted(event.firstName());

        log.info("Отправка приветственного Email: userId={}", user.id());
        notificationService.sendEmail(user.email(), subject, message);
        log.info("Приветственное уведомление успешно отправлено: userId={}", user.id());
    }
}