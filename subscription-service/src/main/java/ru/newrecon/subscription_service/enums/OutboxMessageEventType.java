package ru.newrecon.subscription_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum OutboxMessageEventType {
    SUBSCRIBE("subscribe-subscription-events");

    private final String topic;
}
