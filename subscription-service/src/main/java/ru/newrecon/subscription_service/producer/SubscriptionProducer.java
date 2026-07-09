package ru.newrecon.subscription_service.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String message) {
        kafkaTemplate.send("sub-events", message);
    }
}
