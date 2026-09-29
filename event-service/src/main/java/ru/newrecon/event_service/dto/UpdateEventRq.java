package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;

public record UpdateEventRq(
    double latitude,
    double longitude,
    String description,
    LocalDateTime startAt,
    int totalParticipants
) {}
