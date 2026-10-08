package ru.newrecon.profile_service.dto;

import java.util.List;
import java.util.UUID;

public record GetProfileViewRs(
    UUID id,
    String name,
    UUID userId,
    String bio,
    String email,
    UUID avatarObjectName,
    List<GetProfileEventRs> profileEventsOwner,
    List<GetProfileEventRs> profileEventsNotOwner
) {}
