package ru.newrecon.event_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum OutboxMessageEventType {
    CREATE("create-event-events"),
    DELETE("delete-event-events");

    private final String topic;
}
