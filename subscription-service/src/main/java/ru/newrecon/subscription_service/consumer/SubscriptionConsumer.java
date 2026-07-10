package ru.newrecon.subscription_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.dto.kafka.CreateEventDto;
import ru.newrecon.subscription_service.service.SubscriptionService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionConsumer {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper kafkObjectMapper;

    @KafkaListener(topics = "profile-events")
    public void listenSub(String message) {
        System.out.println("recieve message from profile-events : " + message);
    }

    @KafkaListener(topics = "create-event-events")
    public void listenCreateEvents(String message) {
        log.info("recieve message from create-event-events : " + message);
        CreateEventDto createEventDto = kafkObjectMapper.readValue(message, CreateEventDto.class);
        subscriptionService.create(createEventDto);
    }

    @KafkaListener(topics = "delete-event-events")
    public void listenDeleteEvents(String message) {
        System.out.println("recieve message from delete-event-events : " + message);
    }
}
