package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

public record CreateEventRq(
    double latitude,
    double longitude,
    String description,
    LocalDateTime startAt,
    LocalTime duration,
    UUID ownerId
) {}
