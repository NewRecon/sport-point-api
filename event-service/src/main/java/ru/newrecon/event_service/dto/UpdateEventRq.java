package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;

import ru.newrecon.event_service.enums.EventStatus;

public record UpdateEventRq(
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    int totalParticipants,
    EventStatus status
) {}
