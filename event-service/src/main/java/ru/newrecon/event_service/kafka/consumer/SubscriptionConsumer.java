package ru.newrecon.event_service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.event_service.kafka.payload.SubscribeSubscriptionPayload;
import ru.newrecon.event_service.service.EventSubscriptionService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class SubscriptionConsumer {

    private final ObjectMapper kafkObjectMapper;
    private final EventSubscriptionService eventSubscriptionService;

    @KafkaListener(topics = "subscribe-subscription-events")
    public void listenSubscribeSubscription(String message) {
        log.info("Получено сообщение из subscribe-subscription-events : " + message);
        SubscribeSubscriptionPayload subscribeSubscriptionDto = kafkObjectMapper.readValue(message, SubscribeSubscriptionPayload.class);
        eventSubscriptionService.create(subscribeSubscriptionDto);
    }
}
