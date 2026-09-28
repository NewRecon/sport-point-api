package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateEventRs(
    UUID id,
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    LocalTime duration,
    UUID ownerId,
    int totalParticipants
) {}
