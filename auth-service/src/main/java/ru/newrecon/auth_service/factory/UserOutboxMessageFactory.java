package ru.newrecon.auth_service.factory;

import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxMessage;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.enums.OutboxMessageEventType;
import ru.newrecon.auth_service.enums.OutboxMessageStatus;
import ru.newrecon.auth_service.kafka.payload.CreateUserPayload;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class UserOutboxMessageFactory {

    private final ObjectMapper objectMapper;

    public OutboxMessage created(User user) {
        UUID idempotencyKey = UUID.randomUUID();

        String payload = objectMapper.writeValueAsString(buildCreateUserDto(user, idempotencyKey));

        OutboxMessage outboxMessage = new OutboxMessage();
        outboxMessage.setEntityId(user.getId());
        outboxMessage.setStatus(OutboxMessageStatus.PENDING);
        outboxMessage.setEventType(OutboxMessageEventType.CREATE);
        outboxMessage.setIdempotencyKey(idempotencyKey);
        outboxMessage.setNextAttemptAt(LocalDateTime.now());
        outboxMessage.setPayload(payload);

        return outboxMessage;
    }

    private CreateUserPayload buildCreateUserDto(User user, UUID idempotencyKey) {
        return new CreateUserPayload(
            user.getId(), user.getUsername(), idempotencyKey
        );
    }
}
