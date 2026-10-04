package ru.newrecon.auth_service.enums;

public enum OutboxMessageStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
