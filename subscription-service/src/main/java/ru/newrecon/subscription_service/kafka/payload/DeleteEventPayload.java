package ru.newrecon.subscription_service.kafka.payload;

import java.util.UUID;

public record DeleteEventPayload(
    UUID eventId
) {}
