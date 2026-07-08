package ru.newrecon.auth_service.dto;

import java.util.UUID;

import ru.newrecon.auth_service.entity.enums.Role;

public record RevokeRoleRq(
    UUID userId,
    Role role
) {}
