package ru.newrecon.auth_service.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import ru.newrecon.auth_service.entity.OutboxEvent;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, UUID> {

    @Query(value = """
            SELECT oe.*
                FROM outbox_event oe
                WHERE oe.status = :status
                AND oe.next_attempt_at <= now()
                ORDER BY oe.next_attempt_at
                LIMIT :limit
                FOR UPDATE SKIP LOCKED
            """,nativeQuery = true)
    List<OutboxEvent> findAllByStatus(String status, int limit);

    @Modifying
    @Transactional 
    @Query (value = """
            UPDATE outbox_event
            SET status = :newStatus
            WHERE id IN (:ids)
            """,nativeQuery = true)
    int updateStatus(List<UUID> ids, String newStatus);

    @Modifying
    @Transactional 
    @Query (value = """
            UPDATE outbox_event
            SET status = :newStatus,
                last_error = :lastError,
                attempt_count = :attemptCount,
                next_attempt_at = :nextAttemptAt
            WHERE id = :uuid
            """,nativeQuery = true)
    int updateAfterSent(UUID uuid, String newStatus, String lastError, int attemptCount, LocalDateTime nextAttemptAt);
}
