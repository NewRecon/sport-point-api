package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record UpdateEventRq(
    double latitude,
    double longitude,
    String description,
    LocalDateTime startAt,
    LocalTime duration,
    int totalParticipants
) {}
