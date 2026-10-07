package ru.newrecon.profile_service.dto;

import java.util.List;
import java.util.UUID;

import ru.newrecon.profile_service.entity.ProfileEvent;

public record ProfileViewDto(
    UUID id,
    String name,
    UUID userId,
    String bio,
    String email,
    List<ProfileEvent> profileEventsOwner,
    List<ProfileEvent> profileEventsNotOwner
) {}
