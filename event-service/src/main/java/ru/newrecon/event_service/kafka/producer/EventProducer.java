package ru.newrecon.event_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EventProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String entityId, String payload) {
        kafkaTemplate.send(topic, entityId, payload);
    }
}
