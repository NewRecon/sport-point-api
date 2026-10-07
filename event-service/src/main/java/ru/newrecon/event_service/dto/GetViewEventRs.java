package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record GetViewEventRs(
    UUID id,
    String title,
    String locationName,
    double latitude,
    double longitude,
    String description,
    LocalDateTime date,
    UUID ownerId,
    String ownerName,
    int totalParticipants,
    List<GetEventSubscriptionViewRs> eventSubscriptions
) {}
