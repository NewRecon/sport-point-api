package ru.newrecon.event_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.newrecon.event_service.enums.EventCategory;
import ru.newrecon.event_service.enums.EventStatus;

@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private String title;
    private String locationName;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime date;
    private UUID ownerId;
    // TODO сюда добавить овнера и заполнять из JWT его String ownerName, а ownerId - для ссылки на профиль
    private int totalParticipants;
    @Enumerated(EnumType.STRING)
    private EventStatus status;
    @Enumerated(EnumType.STRING)
    private EventCategory category;
}
