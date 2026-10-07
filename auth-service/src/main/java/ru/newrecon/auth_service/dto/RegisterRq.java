package ru.newrecon.auth_service.dto;

public record RegisterRq(
    String username,
    String email,
    String password
) {}
