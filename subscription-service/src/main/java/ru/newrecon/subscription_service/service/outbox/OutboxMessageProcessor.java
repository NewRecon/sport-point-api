package ru.newrecon.subscription_service.service.outbox;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.subscription_service.entity.OutboxMessage;
import ru.newrecon.subscription_service.enums.OutboxMessageStatus;
import ru.newrecon.subscription_service.kafka.producer.SubscriptionProducer;

@Service 
@RequiredArgsConstructor 
public class OutboxMessageProcessor {

    private final OutboxMessageService outboxMessageService;
    private final SubscriptionProducer subscriptionProducer;

    @Value("${outbox.max-attemt}")
    private int maxAttemptCount;
    @Value("${outbox.attempt-delay-sec}")
    private long attemptDelay;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void process(OutboxMessage outboxMessage) {
        try {
            subscriptionProducer.send(
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
