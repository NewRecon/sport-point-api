package ru.newrecon.event_service.service;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.event_service.entity.Event;
import ru.newrecon.event_service.entity.OutboxMessage;
import ru.newrecon.event_service.enums.OutboxMessageEventType;
import ru.newrecon.event_service.kafka.payload.CreateEventPayload;
import ru.newrecon.event_service.kafka.payload.DeleteEventPayload;
import ru.newrecon.event_service.service.outbox.OutboxMessageService;
import tools.jackson.databind.ObjectMapper;

@Service 
@RequiredArgsConstructor 
public class EventOutboxFacade {

    private final OutboxMessageService outboxMessageService;
    private final ObjectMapper objectMapper;

    public void saveCreateEventEvent(Event event) {
        UUID idempotencyKey = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(buildCreateEventPayload(event));
        OutboxMessage outboxMessage = outboxMessageService.create(
            event.getId(), payload, idempotencyKey, OutboxMessageEventType.CREATE
        );
        outboxMessageService.save(outboxMessage);
    }

    public void saveDeleteEventEvent(Event event) {
        UUID idempotencyKey = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(buildDeleteEventPayload(event));
        OutboxMessage outboxMessage = outboxMessageService.create(
            event.getId(), payload, idempotencyKey, OutboxMessageEventType.DELETE
        );
        outboxMessageService.save(outboxMessage);
    }


    private CreateEventPayload buildCreateEventPayload(Event event) {
        return new CreateEventPayload(
            event.getId(), event.getOwnerId(), event.getTotalParticipants()
        );
    }

    private DeleteEventPayload buildDeleteEventPayload(Event event) {
        return new DeleteEventPayload(event.getId());
    }
}
