package ru.newrecon.profile_service.entity;

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
import ru.newrecon.profile_service.enums.ParticipantRole;

@Getter
@Setter
@Entity
public class SubscriptionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private LocalDateTime startedAt;
    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;
}
