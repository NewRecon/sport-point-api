package ru.newrecon.subscription_service.dto;

import java.util.UUID;

import ru.newrecon.subscription_service.enums.ParticipantRole;

public record UpdateSubscriptionRq(
    UUID eventId,
    ParticipantRole participantRole
) {}
