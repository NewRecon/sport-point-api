package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;

public record CreateEventRq(
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    int totalParticipants
) {}
