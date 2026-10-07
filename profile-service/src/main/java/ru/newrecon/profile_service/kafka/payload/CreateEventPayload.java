package ru.newrecon.profile_service.kafka.payload;

import java.util.UUID;

public record CreateEventPayload(
    UUID eventId,
    UUID userId,
    int totalParticipants,
    boolean isCreatorParticipant,
    String eventTitle
) {}
