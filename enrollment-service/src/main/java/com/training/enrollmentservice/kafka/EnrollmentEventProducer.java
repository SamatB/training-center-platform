package com.training.enrollmentservice.kafka;

import com.training.enrollmentservice.event.EnrollmentCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnrollmentEventProducer {

    private final KafkaTemplate<String, EnrollmentCreatedEvent> kafkaTemplate;

    @Value("${app.kafka.topics.enrollment-created}")
    private String enrollmentCreatedTopic;

    public void sendEnrollmentCreatedEvent(EnrollmentCreatedEvent event) {

        log.info("Отправка EnrollmentCreatedEvent в топик {}: {}", enrollmentCreatedTopic, event);

        kafkaTemplate.send(enrollmentCreatedTopic, event)
                .whenComplete((result, ex) -> {
                    if (ex == null) {
                        log.info("EnrollmentCreatedEvent успешно записана в топик = {}, партиция = {}, offset = {}",
                                result.getRecordMetadata().topic(),
                                result.getRecordMetadata().partition(),
                                result.getRecordMetadata().offset());
                    } else {
                        log.error("Провал отправки EnrollmentCreatedEvent: {}", event, ex);
                    }
                });

    }
}