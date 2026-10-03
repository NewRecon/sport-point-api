package ru.newrecon.subscription_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.entity.Subscription;
import ru.newrecon.subscription_service.kafka.payload.SubscribeSubscriptionPayload;
import ru.newrecon.subscription_service.kafka.producer.SubscriptionProducer;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionSendService {

    private final SubscriptionProducer subscriptionProducer;
    private final ObjectMapper kafkObjectMapper;

    public void sendSubscribe(Subscription subscription, String username) {

        SubscribeSubscriptionPayload createEventDto = new SubscribeSubscriptionPayload(
            subscription.getUserId(), subscription.getEventId(), username
        );

        String kafkaMessage = kafkObjectMapper.writeValueAsString(createEventDto);
        subscriptionProducer.sendCreate(kafkaMessage);
    }
}
