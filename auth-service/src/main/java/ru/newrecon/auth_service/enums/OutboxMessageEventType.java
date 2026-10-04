package ru.newrecon.auth_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum OutboxMessageEventType {
    CREATE("create-user-events");

    private final String topic;
}
