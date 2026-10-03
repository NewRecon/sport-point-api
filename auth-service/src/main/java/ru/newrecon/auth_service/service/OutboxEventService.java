package ru.newrecon.auth_service.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxEvent;
import ru.newrecon.auth_service.entity.enums.OutboxStatus;
import ru.newrecon.auth_service.repository.OutboxEventRepository;

@Service 
@RequiredArgsConstructor 
public class OutboxEventService {

    private final OutboxEventRepository outboxEventRepository;

    public void save(OutboxEvent outboxEvent) {
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional
    public List<OutboxEvent> updatePendingStatusOnProcessing(int limit) {
        List<OutboxEvent> outboxEvents = outboxEventRepository.findAllByStatus(OutboxStatus.PENDING.name(), limit);
        outboxEventRepository.updateStatus(outboxEvents.stream().map(OutboxEvent::getId).toList(), OutboxStatus.PROCESSING.name());

        return outboxEvents;
    }

    public void updateAfterSent(OutboxEvent outboxEvent) {
        outboxEventRepository.updateAfterSent(
            outboxEvent.getId(), outboxEvent.getStatus().name(), outboxEvent.getLastError(),
            outboxEvent.getAttemptCount(), outboxEvent.getNextAttemptAt()
        );
    }
}
