package ru.newrecon.event_service.enums;

public enum OutboxMessageStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
