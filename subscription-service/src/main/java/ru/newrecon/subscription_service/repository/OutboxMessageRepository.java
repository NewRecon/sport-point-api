package ru.newrecon.subscription_service.repository;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import ru.newrecon.subscription_service.entity.OutboxMessage;
import ru.newrecon.subscription_service.enums.OutboxMessageStatus;

public interface OutboxMessageRepository extends JpaRepository<OutboxMessage, UUID> {

    @Query(value = """
            SELECT oe.*
                FROM outbox_message oe
                WHERE oe.status = :status
                AND oe.next_attempt_at <= now()
                ORDER BY oe.next_attempt_at
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            """,nativeQuery = true)
    List<OutboxMessage> findAllByStatus(String status, int limit);

    @Modifying
    @Transactional 
    @Query (value = """
            UPDATE OutboxMessage e 
            SET e.status = :newStatus
            WHERE id IN (:ids)
            """)
    int updateStatus(List<UUID> ids, OutboxMessageStatus newStatus);

    @Modifying
    @Transactional 
    @Query (value = """
            UPDATE OutboxMessage e 
            SET e.status = :#{#outboxMessage.status}, 
                e.lastError = :#{#outboxMessage.lastError}, 
                e.attemptCount = :#{#outboxMessage.attemptCount}, 
                e.nextAttemptAt = :#{#outboxMessage.nextAttemptAt} 
            WHERE e.id = :#{#outboxMessage.id}
            """)
    int updateAfterSent(OutboxMessage outboxMessage);

    @Modifying
    @Transactional 
    @Query (value = """
            DELETE FROM OutboxMessage e
            WHERE e.status = :status
            """)
    int deleteByStatus(OutboxMessageStatus status);
}
