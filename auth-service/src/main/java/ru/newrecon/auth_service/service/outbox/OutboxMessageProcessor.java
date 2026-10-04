package ru.newrecon.auth_service.service.outbox;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxMessage;
import ru.newrecon.auth_service.enums.OutboxMessageStatus;
import ru.newrecon.auth_service.kafka.producer.UserProducer;

@Service 
@RequiredArgsConstructor 
public class OutboxMessageProcessor {

    private final OutboxMessageService outboxMessageService;
    private final UserProducer userProducer;

    @Value("${outbox.max-attemt}")
    private int maxAttemptCount;
    @Value("${outbox.attempt-delay-sec}")
    private long attemptDelay;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(OutboxMessage outboxMessage) {
        try {
            userProducer.send(
                outboxMessage.getEventType().getTopic(), outboxMessage.getEntityId().toString(), outboxMessage.getPayload()
            );
            outboxMessage.setStatus(OutboxMessageStatus.SENT);
        } catch (Exception e) {
            outboxMessage.setLastError(e.getMessage());
            outboxMessage.setAttemptCount(outboxMessage.getAttemptCount()+1);
            if(outboxMessage.getAttemptCount() >= maxAttemptCount) {
                outboxMessage.setStatus(OutboxMessageStatus.FAILED);
            } else {
                outboxMessage.setStatus(OutboxMessageStatus.PENDING);
                outboxMessage.setNextAttemptAt(LocalDateTime.now().plusSeconds(attemptDelay));
            }
            
        }

        outboxMessageService.updateAfterSent(outboxMessage);
    }
}
