package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;

import ru.newrecon.event_service.enums.EventCategory;

public record CreateEventRq(
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    int totalParticipants,
    EventCategory category,
    boolean isCreatorParticipant
) {}
