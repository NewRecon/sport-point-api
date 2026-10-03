package ru.newrecon.subscription_service.dto.kafka;

import java.util.UUID;

public record SubscribeSubscriptionDto(
    UUID userId,
    UUID eventId,
    String username
) {}
