package ru.newrecon.auth_service.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxMessage;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.factory.UserOutboxMessageFactory;
import ru.newrecon.auth_service.service.outbox.OutboxMessageService;

@Service 
@RequiredArgsConstructor 
public class UserOutboxFacade {

    private final UserService userService;
    private final OutboxMessageService outboxMessageService;
    private final UserOutboxMessageFactory userOutboxMessageFactory;

    @Transactional
    public UserDetails create(User user) {
        UserDetails userdetails = userService.save(user);

        OutboxMessage outboxMessage = userOutboxMessageFactory.created(user);
        outboxMessageService.save(outboxMessage);

        return userdetails;
    }
}
