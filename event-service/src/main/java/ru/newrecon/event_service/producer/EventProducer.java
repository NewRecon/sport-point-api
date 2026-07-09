package ru.newrecon.event_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send("event-events", message);
    }
}
