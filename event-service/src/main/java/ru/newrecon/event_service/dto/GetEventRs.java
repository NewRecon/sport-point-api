package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import ru.newrecon.event_service.enums.EventCategory;

public record GetEventRs(
    UUID id,
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    UUID ownerId,
    int totalParticipants,
    EventCategory category
) {}
