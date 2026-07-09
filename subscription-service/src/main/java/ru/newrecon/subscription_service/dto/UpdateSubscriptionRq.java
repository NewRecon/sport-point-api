package ru.newrecon.subscription_service.dto;

import java.util.UUID;

public record UpdateSubscriptionRq(
    UUID userId,
    UUID eventId
) {}
