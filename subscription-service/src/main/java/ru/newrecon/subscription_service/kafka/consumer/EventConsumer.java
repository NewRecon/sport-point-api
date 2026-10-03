package ru.newrecon.subscription_service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.kafka.payload.CreateEventPayload;
import ru.newrecon.subscription_service.kafka.payload.DeleteEventPayload;
import ru.newrecon.subscription_service.service.SubscriptionService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class EventConsumer {

    private final SubscriptionService subscriptionService;
    private final ObjectMapper kafkObjectMapper;

    @KafkaListener(topics = "create-event-events")
    public void listenCreateEvents(String message) {
        log.info("Получено сообщение из create-event-events : " + message);
        CreateEventPayload createEventDto = kafkObjectMapper.readValue(message, CreateEventPayload.class);
        subscriptionService.create(createEventDto);
    }

    @KafkaListener(topics = "delete-event-events")
    public void listenDeleteEvents(String message) {
        System.out.println("Получено сообщение из delete-event-events : " + message);
        DeleteEventPayload deleteEventDto = kafkObjectMapper.readValue(message, DeleteEventPayload.class);
        subscriptionService.deleteByEventId(deleteEventDto.eventId());
    }
}
