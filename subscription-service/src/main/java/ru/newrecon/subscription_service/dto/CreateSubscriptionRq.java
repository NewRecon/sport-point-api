package ru.newrecon.subscription_service.dto;

import java.util.UUID;

import ru.newrecon.subscription_service.entity.enums.ParticipantRole;

public record CreateSubscriptionRq(
    UUID eventId,
    ParticipantRole participantRole
) {}
