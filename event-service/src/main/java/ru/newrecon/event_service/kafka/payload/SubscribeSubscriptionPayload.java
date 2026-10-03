package ru.newrecon.event_service.kafka.payload;

import java.util.UUID;

public record SubscribeSubscriptionPayload(
    UUID userId,
    UUID eventId,
    String username
) {}
