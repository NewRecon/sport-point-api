package ru.newrecon.auth_service.dto;

public record LoginRq(
    String username,
    String password
) {}
