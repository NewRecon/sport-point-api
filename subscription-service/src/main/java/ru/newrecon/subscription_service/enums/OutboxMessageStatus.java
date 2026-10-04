package ru.newrecon.subscription_service.enums;

public enum OutboxMessageStatus {
    PENDING,
    PROCESSING,
    SENT,
    FAILED
}
