package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import ru.newrecon.event_service.entity.EventSubscription;

public record EventViewDto(
    UUID id,
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    UUID ownerId,
    int totalParticipants,
    List<EventSubscription> eventSubscriptions
) {}
