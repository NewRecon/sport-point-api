package ru.newrecon.event_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCreate(String message) {
        kafkaTemplate.send("create-event-events", message);
    }

    public void sendDelete(String message) {
        kafkaTemplate.send("delete-event-events", message);
    }
}
