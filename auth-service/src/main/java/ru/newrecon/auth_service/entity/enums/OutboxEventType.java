package ru.newrecon.auth_service.entity.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum OutboxEventType {
    CREATE("create-user-events");

    private final String topic;
}
