package ru.newrecon.auth_service.service.outbox;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxMessage;

@Service 
@RequiredArgsConstructor 
public class OutboxDispatcher {

    private final OutboxMessageService outboxMessageService;
    private final OutboxMessageProcessor outboxMessageProcessor;

    @Value("${outbox.batch-size}")
    private Integer outboxBatchSize;

    public void runSendProcess() {
        List<OutboxMessage> outboxMessage = outboxMessageService.updatePendingStatusOnProcessing(outboxBatchSize);

        outboxMessage.stream()
            .forEach(eventId -> {
                outboxMessageProcessor.process(eventId);
            });
    }
}
