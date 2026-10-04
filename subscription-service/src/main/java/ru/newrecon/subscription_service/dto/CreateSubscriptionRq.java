package ru.newrecon.subscription_service.dto;

import java.util.UUID;

import ru.newrecon.subscription_service.enums.ParticipantRole;

public record CreateSubscriptionRq(
    UUID eventId,
    ParticipantRole participantRole
) {}
