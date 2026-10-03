package ru.newrecon.auth_service.service.outbox;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxEvent;
import ru.newrecon.auth_service.service.OutboxEventService;

@Service 
@RequiredArgsConstructor 
public class OutboxDispatcher {

    private final OutboxEventService outboxEventService;
    private final OutboxMessageProcessor outboxMessageProcessor;

    @Value("${outbox.bath-size}")
    private int outboxBatchSize;

    public void sendAll() {
        List<OutboxEvent> outboxEventsIds = outboxEventService.updatePendingStatusOnProcessing(outboxBatchSize);

        while(outboxEventsIds.size() > 0) {
            outboxEventsIds.stream()
                .forEach(eventId -> {
                    outboxMessageProcessor.process(eventId);
                });

            outboxEventsIds = outboxEventService.updatePendingStatusOnProcessing(outboxBatchSize);
        }
    }
}
