package ru.newrecon.profile_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class ProfileConsumer {

    @KafkaListener(topics = "sub-events")
    public void listenSub(String message) {
        System.out.println("recieve message from sub-events : " + message);
    }

    @KafkaListener(topics = "event-events")
    public void listenEvents(String message) {
        System.out.println("recieve message from event-events : " + message);
    }
}
