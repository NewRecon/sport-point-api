package ru.newrecon.event_service.dto.kafka;

import java.util.UUID;

public record DeleteEventDto(
    UUID eventId
) {}
