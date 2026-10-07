package ru.newrecon.auth_service.dto;

public record RegisterRq(
    String username,
    String name,
    String email,
    String password
) {}
