package ru.newrecon.subscription_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class SubscriptionConsumer {

    @KafkaListener(topics = "profile-events")
    public void listenSub(String message) {
        System.out.println("recieve message from profile-events : " + message);
    }

    @KafkaListener(topics = "event-events")
    public void listenEvents(String message) {
        System.out.println("recieve message from event-events : " + message);
    }
}
