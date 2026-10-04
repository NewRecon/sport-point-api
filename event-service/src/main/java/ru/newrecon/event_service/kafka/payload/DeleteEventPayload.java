package ru.newrecon.event_service.kafka.payload;

import java.util.UUID;

public record DeleteEventPayload(
    UUID eventId,
    UUID idempotencyKey
) {}
