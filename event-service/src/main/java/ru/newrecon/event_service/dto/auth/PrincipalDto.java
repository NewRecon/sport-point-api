package ru.newrecon.event_service.dto.auth;

import java.util.UUID;

public record PrincipalDto(
    UUID userId,
    String username
) {}
