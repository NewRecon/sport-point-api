package ru.newrecon.event_service.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

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
    int currentParticipants,
    List<Participant> participants
) {}
