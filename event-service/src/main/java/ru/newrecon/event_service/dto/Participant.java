package ru.newrecon.event_service.dto;

import java.util.UUID;

public record Participant(
    UUID id,
    String name
) {}
