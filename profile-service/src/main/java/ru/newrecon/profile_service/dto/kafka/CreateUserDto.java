package ru.newrecon.profile_service.dto.kafka;

import java.util.UUID;

public record CreateUserDto(
    UUID userId,
    String name
) {}
