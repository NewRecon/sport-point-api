package ru.newrecon.profile_service.dto;

import java.util.UUID;

public record CreateProfileRq(
    String name,
    UUID userId
) {}
