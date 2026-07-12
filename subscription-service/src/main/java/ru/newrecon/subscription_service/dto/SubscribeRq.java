package ru.newrecon.subscription_service.dto;

import java.util.UUID;

public record SubscribeRq(
    UUID eventId
) {}
