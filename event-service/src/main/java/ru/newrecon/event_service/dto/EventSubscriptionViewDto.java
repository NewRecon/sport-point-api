package ru.newrecon.event_service.dto;

import java.util.UUID;

public record EventSubscriptionViewDto(
    UUID userId,
    String username
) {}
