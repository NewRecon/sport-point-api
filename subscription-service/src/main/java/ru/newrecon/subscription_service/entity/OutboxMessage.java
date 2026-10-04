package ru.newrecon.subscription_service.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import ru.newrecon.subscription_service.enums.OutboxMessageEventType;
import ru.newrecon.subscription_service.enums.OutboxMessageStatus;

@Getter 
@Setter 
@Entity 
@EntityListeners(AuditingEntityListener.class)
public class OutboxMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;
    private UUID entityId;
    @Enumerated(EnumType.STRING)
    private OutboxMessageEventType eventType;
    @JdbcTypeCode(SqlTypes.JSON)
    private String payload; 
    @Enumerated(EnumType.STRING)
    private OutboxMessageStatus status;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;
    @ColumnDefault("0")
    private int attemptCount;
    private LocalDateTime nextAttemptAt;
    private String lastError;
    private UUID idempotencyKey;
}
