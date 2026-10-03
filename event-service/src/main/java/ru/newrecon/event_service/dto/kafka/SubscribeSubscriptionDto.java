package ru.newrecon.event_service.dto.kafka;

import java.util.UUID;

public record SubscribeSubscriptionDto(
    UUID userId,
    UUID eventId,
    String username
) {}
