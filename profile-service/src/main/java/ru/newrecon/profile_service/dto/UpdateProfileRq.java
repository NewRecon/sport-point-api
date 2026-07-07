package ru.newrecon.profile_service.dto;

import java.util.UUID;

public record UpdateProfileRq(
    String name,
    UUID userId
) {}
