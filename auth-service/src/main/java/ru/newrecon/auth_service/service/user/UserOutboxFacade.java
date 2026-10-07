package ru.newrecon.auth_service.service.user;

import java.util.UUID;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.enums.OutboxMessageEventType;
import ru.newrecon.auth_service.kafka.payload.CreateUserPayload;
import ru.newrecon.auth_service.service.outbox.OutboxMessageService;
import tools.jackson.databind.ObjectMapper;

@Service 
@RequiredArgsConstructor 
public class UserOutboxFacade {

    private final OutboxMessageService outboxMessageService;
    private final ObjectMapper objectMapper;

    public void saveCreateUserEvent(User user, String email) {
        UUID idempotencyKey = UUID.randomUUID();
        String payload = objectMapper.writeValueAsString(buildCreateUserPayload(user, email));
        outboxMessageService.create(
            user.getId(), payload, idempotencyKey, OutboxMessageEventType.CREATE
        );
    }

    private CreateUserPayload buildCreateUserPayload(User user, String email) {
        return new CreateUserPayload(
            user.getId(), email, user.getName()
        );
    }
}
