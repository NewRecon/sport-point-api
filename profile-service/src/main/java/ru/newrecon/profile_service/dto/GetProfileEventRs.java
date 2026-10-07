package ru.newrecon.profile_service.dto;

import java.util.UUID;

public record GetProfileEventRs(
    UUID eventId,
    String eventTitle
) {}
