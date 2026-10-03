package ru.newrecon.auth_service.kafka.payload;

import java.util.UUID;

public record CreateUserPayload(
    UUID userId,
    String name,
    UUID idempotencyKey
) {}
