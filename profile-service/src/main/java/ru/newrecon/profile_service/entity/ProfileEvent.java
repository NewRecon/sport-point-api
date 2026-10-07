package ru.newrecon.profile_service.entity;

import java.time.LocalDateTime;
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
public class ProfileEvent {
    @Id 
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private String eventTitle;
    private LocalDateTime eventDate;
    private boolean isOwner;
    private boolean isCreatorParticipant;
}
