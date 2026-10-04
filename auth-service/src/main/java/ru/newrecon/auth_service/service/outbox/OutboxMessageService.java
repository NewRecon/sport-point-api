package ru.newrecon.auth_service.service.outbox;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxMessage;
import ru.newrecon.auth_service.enums.OutboxMessageEventType;
import ru.newrecon.auth_service.enums.OutboxMessageStatus;
import ru.newrecon.auth_service.repository.OutboxMessageRepository;

@Service 
@RequiredArgsConstructor 
public class OutboxMessageService {

    private final OutboxMessageRepository outboxMessageRepository;

    public void save(OutboxMessage outboxMessage) {
        outboxMessageRepository.save(outboxMessage);
    }

    public OutboxMessage create(UUID entityId, String payload, UUID idempotencyKey, OutboxMessageEventType eventType) {
        OutboxMessage outboxMessage = new OutboxMessage();
        outboxMessage.setEntityId(entityId);
        outboxMessage.setStatus(OutboxMessageStatus.PENDING);
        outboxMessage.setEventType(eventType);
        outboxMessage.setIdempotencyKey(idempotencyKey);
        outboxMessage.setNextAttemptAt(LocalDateTime.now());
        outboxMessage.setPayload(payload);

        return outboxMessage;
    }

    @Transactional
    public List<OutboxMessage> updatePendingStatusOnProcessing(int limit) {
        List<OutboxMessage> outboxMessage = outboxMessageRepository.findAllByStatus(OutboxMessageStatus.PENDING.name(), limit);
        outboxMessageRepository.updateStatus(outboxMessage.stream().map(OutboxMessage::getId).toList(), OutboxMessageStatus.PROCESSING);

        return outboxMessage;
    }

    public void updateAfterSent(OutboxMessage outboxMessage) {
        outboxMessageRepository.updateAfterSent(outboxMessage);
    }

    public void deleteAllInSentStatus() {
        outboxMessageRepository.deleteByStatus(OutboxMessageStatus.SENT);
    }
}
