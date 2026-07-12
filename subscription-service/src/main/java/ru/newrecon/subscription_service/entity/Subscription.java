package ru.newrecon.subscription_service.entity;

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
import ru.newrecon.subscription_service.entity.enums.ParticipantRole;

@Getter
@Setter
@Entity
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID userId;
    private UUID eventId;
    private LocalDateTime createAt;
    @Enumerated(EnumType.STRING)
    private ParticipantRole participantRole;
}
