package ru.newrecon.auth_service.dto;

import java.util.UUID;

import ru.newrecon.auth_service.enums.UserRole;

public record AssignRoleRq(
    UUID userId,
    UserRole role
) {}
