package com.training.notificationservice.kafka;

<<<<<<< HEAD
=======
import com.training.notificationservice.client.UserClient;
import com.training.notificationservice.client.dto.UserResponse;
import com.training.notificationservice.event.UserRegisteredEvent;
import com.training.notificationservice.service.NotificationService;
>>>>>>> main
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
<<<<<<< HEAD
import com.training.notificationservice.client.UserClient;
import com.training.notificationservice.client.dto.UserResponse;
import com.training.notificationservice.service.NotificationService;
import com.training.notificationservice.event.UserRegisteredEvent;
=======
>>>>>>> main

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

<<<<<<< HEAD
        log.info("Получено UserRegisteredEvent: userId={}, email={}",
                event.userId(), event.email());

        log.info("Получение профиля пользователя из user-service: userId={}",
                event.userId());

        UserResponse user = userClient.getUserById(event.userId());

        log.info("Профиль пользователя получен: userId={}, email={}",
                user.id(), user.email());
=======
        log.info("Получено UserRegisteredEvent: userId={}, email={}", event.userId(), event.email());
        log.info("Получение профиля пользователя из user-service: userId={}", event.userId());

        UserResponse user = userClient.getUserById(event.userId());

        log.info("Профиль пользователя получен: userId={}, email={}", user.id(), user.email());
>>>>>>> main

        String subject = "Добро пожаловать в Training Center Platform";
        String message = """
                Здравствуйте, %s!
                Вы успешно зарегистрировались в Training Center Platform.
                Желаем успешного обучения!
                """.formatted(event.firstName());

        log.info("Отправка приветственного Email: userId={}", user.id());
<<<<<<< HEAD

        notificationService.sendEmail(user.email(), subject, message);

=======
        notificationService.sendEmail(user.email(), subject, message);
>>>>>>> main
        log.info("Приветственное уведомление успешно отправлено: userId={}", user.id());
    }
}