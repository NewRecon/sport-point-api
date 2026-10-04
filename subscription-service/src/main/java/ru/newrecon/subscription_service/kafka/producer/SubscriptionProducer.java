package ru.newrecon.subscription_service.kafka.producer;

import java.util.concurrent.TimeUnit;

import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String entityId, String payload) {
        try {
            kafkaTemplate.send(topic,entityId, payload)
                .get(5, TimeUnit.SECONDS);
        } catch (Exception e) {
            throw new KafkaException(e.getMessage(), e.getCause());
        }
    }
}
