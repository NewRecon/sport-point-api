package ru.newrecon.subscription_service.kafka.producer;

import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void sendCreate(String message) {
        kafkaTemplate.send("subscribe-subscription-events", message);
    }
}
