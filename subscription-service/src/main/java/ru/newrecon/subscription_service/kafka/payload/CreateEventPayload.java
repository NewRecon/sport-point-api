package ru.newrecon.subscription_service.kafka.payload;

import java.util.UUID;

public record CreateEventPayload(
    UUID eventId,
    UUID userId,
    int totalParticipants
) {}
