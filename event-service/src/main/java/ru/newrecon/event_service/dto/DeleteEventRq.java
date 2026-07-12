package ru.newrecon.event_service.dto;

import java.util.UUID;

public record DeleteEventRq(
    UUID eventId
) {}
