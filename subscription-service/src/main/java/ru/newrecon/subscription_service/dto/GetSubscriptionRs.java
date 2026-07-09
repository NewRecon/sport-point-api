package ru.newrecon.subscription_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetSubscriptionRs(
    UUID id,
    UUID userId,
    UUID eventId,
    LocalDateTime createAt
) {}
