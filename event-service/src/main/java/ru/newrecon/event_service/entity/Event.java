package ru.newrecon.event_service.entity;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.UUID;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private double latitude;
    private double longitude;
    private String description;
    private LocalDateTime startAt;
    private LocalTime duration;
    private UUID ownerId;
    private int totalParticipants;
}
