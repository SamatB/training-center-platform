package com.training.authservice.kafka;
import com.training.authservice.event.UserRegisteredEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
@Component
@RequiredArgsConstructor
@Slf4j

public class UserEventProducer {

    private final KafkaTemplate<String, UserRegisteredEvent> kafkaTemplate;
    @Value("${app.kafka.topics.user-registered}")
    private String userRegisteredTopic;

    public void sendUserRegisteredEvent(UserRegisteredEvent event) {
        log.info("Отправка UserRegisteredEvent для userId={}", event.userId());

        kafkaTemplate.send(userRegisteredTopic, event.userId().toString(), event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {

                        log.info("Успешно отправлено в topic={}, partition={}, offset={}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());


                    } else {
                        log.error("Ошибка отправки UserRegisteredEvent для userId={}",
                                event.userId(), ex);

                    }
                });
    }

}


