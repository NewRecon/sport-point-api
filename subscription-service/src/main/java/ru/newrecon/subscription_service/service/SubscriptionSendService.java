package ru.newrecon.subscription_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.subscription_service.dto.kafka.SubscribeSubscriptionDto;
import ru.newrecon.subscription_service.entity.Subscription;
import ru.newrecon.subscription_service.producer.SubscriptionProducer;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionSendService {

    private final SubscriptionProducer subscriptionProducer;
    private final ObjectMapper kafkObjectMapper;

    public void sendSubscribe(Subscription subscription) {

        SubscribeSubscriptionDto createEventDto = new SubscribeSubscriptionDto(
            subscription.getUserId(), subscription.getEventId()
        );

        String kafkaMessage = kafkObjectMapper.writeValueAsString(createEventDto);
        subscriptionProducer.sendCreate(kafkaMessage);
    }
}
