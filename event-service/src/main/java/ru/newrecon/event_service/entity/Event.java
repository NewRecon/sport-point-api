package ru.newrecon.event_service.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Event {
    @Id
    private UUID id;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime startAt;
    private LocalTime duration;
    private UUID ownerId;
}
