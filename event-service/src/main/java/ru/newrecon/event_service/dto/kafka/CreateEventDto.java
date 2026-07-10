package ru.newrecon.event_service.dto.kafka;

import java.util.UUID;

public record CreateEventDto(
    UUID eventId,
    UUID userId,
    int totalParticipants
) {}
