package ru.newrecon.auth_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCreate(String message) {
        kafkaTemplate.send("create-user-events", message);
    }
}
