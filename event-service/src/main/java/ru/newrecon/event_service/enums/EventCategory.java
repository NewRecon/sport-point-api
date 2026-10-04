package ru.newrecon.event_service.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter 
@RequiredArgsConstructor 
public enum EventCategory {
    EXERCISE("тренировка"),
    COMPETITION("соревнование"),
    GAME("игра"),
    MARATHON("марафон"),
    FESTIVAL("фестиваль");

    private final String title;
}
