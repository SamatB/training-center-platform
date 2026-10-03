package com.training.userservice.kafka;
import com.training.userservice.entity.User;
import com.training.userservice.event.UserRegisteredEvent;
import com.training.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.kafka.annotation.KafkaListener;
import java.time.LocalDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class UserRegisteredListener {

    private final UserRepository userRepository;

    @KafkaListener(
            topics ="${app.kafka.topics.user-registered}",
            groupId = "user-service"
    )
    public void listen(UserRegisteredEvent event){
        log.info("Получено событие UserRegisteredEvent userId={}", event.userId());

        boolean exists = userRepository.existsById(event.userId());

        if (exists) {
            log.info("Пользователь userId={} уже существует, пропускаем", event.userId());

            return;
        }
        User user = User.builder()
                .id(event.userId())
                .firstName(event.firstName())
                .lastName(event.lastName())
                .email(event.email())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        userRepository.save(user);
        log.info("Профиль пользователя userId={} создан", event.userId());

    }
}
