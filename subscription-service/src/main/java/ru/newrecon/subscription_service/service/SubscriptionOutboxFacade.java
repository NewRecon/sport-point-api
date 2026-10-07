package ru.newrecon.subscription_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.subscription_service.entity.OutboxMessage;
import ru.newrecon.subscription_service.entity.Subscription;
import ru.newrecon.subscription_service.enums.OutboxMessageEventType;
import ru.newrecon.subscription_service.kafka.payload.SubscribeSubscriptionPayload;
import ru.newrecon.subscription_service.service.outbox.OutboxMessageService;
import tools.jackson.databind.ObjectMapper;

@Service 
@RequiredArgsConstructor 
public class SubscriptionOutboxFacade {

    private final OutboxMessageService outboxMessageService;
    private final ObjectMapper objectMapper;

    public void saveSubscribeSubscriptionEvent(Subscription subscription, String name) {
        UUID idempotencyKey = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(buidSubscribeSubscriptionPayload(subscription, name));
        OutboxMessage outboxMessage = outboxMessageService.create(
            subscription.getId(), payload, idempotencyKey, OutboxMessageEventType.SUBSCRIBE
        );
        outboxMessageService.save(outboxMessage);
    }

    private SubscribeSubscriptionPayload buidSubscribeSubscriptionPayload(Subscription subscription, String name) {
        return new SubscribeSubscriptionPayload(
            subscription.getUserId(), subscription.getEventId(), name
        );
    }
}
