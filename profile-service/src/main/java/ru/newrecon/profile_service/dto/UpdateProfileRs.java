package ru.newrecon.profile_service.dto;

import java.util.UUID;

public record UpdateProfileRs(
    UUID id,
    String name,
    UUID userId,
    String bio
) {}
