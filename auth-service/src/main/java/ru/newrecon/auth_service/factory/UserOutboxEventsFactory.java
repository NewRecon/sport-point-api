package ru.newrecon.auth_service.factory;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxEvent;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.entity.enums.OutboxEventType;
import ru.newrecon.auth_service.entity.enums.OutboxStatus;
import ru.newrecon.auth_service.kafka.payload.CreateUserPayload;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UserOutboxEventsFactory {

    private final ObjectMapper objectMapper;

    public OutboxEvent created(User user) {

        UUID idempotencyKey = UUID.randomUUID();

        String payload = objectMapper.writeValueAsString(buildCreateUserDto(user, idempotencyKey));

        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setEntityId(user.getId());
        outboxEvent.setStatus(OutboxStatus.PENDING);
        outboxEvent.setEventType(OutboxEventType.CREATE);
        outboxEvent.setIdempotencyKey(idempotencyKey);
        outboxEvent.setNextAttemptAt(LocalDateTime.now());
        outboxEvent.setPayload(payload);

        return outboxEvent;
    }

    private CreateUserPayload buildCreateUserDto(User user, UUID idempotencyKey) {
        return new CreateUserPayload(
            user.getId(), user.getUsername(), idempotencyKey
        );
    }
}
