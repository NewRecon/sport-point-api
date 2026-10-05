package ru.newrecon.subscription_service.kafka.producer;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.KafkaException;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SubscriptionProducer {

    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String topic, String entityId, String payload, String idempotencyKey) {
        try {
            ProducerRecord<String, String> record = new ProducerRecord<>(topic, entityId, payload);
            record.headers().add(
                    "idempotency-key",
                    idempotencyKey.getBytes(StandardCharsets.UTF_8));

            kafkaTemplate.send(record).get(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new KafkaException("Отправка прервана: topic=" + topic, e);
        } catch (Exception e) {
            throw new KafkaException("Не удалось отправить сообщение: topic=" + topic, e);
        }
    }
}
