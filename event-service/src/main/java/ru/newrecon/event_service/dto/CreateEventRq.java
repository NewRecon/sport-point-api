package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record CreateEventRq(
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    LocalTime duration,
    int totalParticipants
) {}
