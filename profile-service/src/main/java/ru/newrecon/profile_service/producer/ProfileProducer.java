package ru.newrecon.profile_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ProfileProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send("profile-events", message);
    }
}
