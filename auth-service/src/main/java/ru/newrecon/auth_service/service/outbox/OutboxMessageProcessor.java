package ru.newrecon.auth_service.service.outbox;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxEvent;
import ru.newrecon.auth_service.entity.enums.OutboxStatus;
import ru.newrecon.auth_service.kafka.producer.UserProducer;
import ru.newrecon.auth_service.service.OutboxEventService;

@Service 
@RequiredArgsConstructor 
public class OutboxMessageProcessor {

    private final OutboxEventService outboxEventService;
    private final UserProducer userProducer;

    @Value("${outbox.max-attemt}")
    private int maxAttemptCount;
    @Value("${outbox.attempt-delay-sec}")
    private long attemptDelay;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(OutboxEvent outboxEvent) {
        try {
            userProducer.send(
                outboxEvent.getEventType().getTopic(), outboxEvent.getEntityId().toString(), outboxEvent.getPayload()
            );
            outboxEvent.setStatus(OutboxStatus.SENT);
        } catch (Exception e) {
            outboxEvent.setLastError(e.getMessage());
            outboxEvent.setAttemptCount(outboxEvent.getAttemptCount()+1);
            if(outboxEvent.getAttemptCount() >= maxAttemptCount) {
                outboxEvent.setStatus(OutboxStatus.FAILED);
            } else {
                outboxEvent.setStatus(OutboxStatus.PENDING);
                outboxEvent.setNextAttemptAt(LocalDateTime.now().plusSeconds(attemptDelay));
            }
            
        }

        outboxEventService.updateAfterSent(outboxEvent);
    }
}
