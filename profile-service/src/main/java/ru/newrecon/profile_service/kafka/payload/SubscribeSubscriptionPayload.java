package ru.newrecon.profile_service.kafka.payload;

import java.util.UUID;

public record SubscribeSubscriptionPayload(
    UUID userId,
    UUID eventId,
    String name,
    String eventTitle
) {}
