package ru.newrecon.auth_service.service.user;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.entity.OutboxEvent;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.factory.UserOutboxEventsFactory;
import ru.newrecon.auth_service.service.OutboxEventService;

@Service 
@RequiredArgsConstructor 
public class UserOutboxFacade {

    private final UserService userService;
    private final OutboxEventService outboxEventService;
    private final UserOutboxEventsFactory userOutboxEventsFactory;

    @Transactional
    public UserDetails create(User user) {
        UserDetails userdetails = userService.save(user);

        OutboxEvent outboxEvent = userOutboxEventsFactory.created(user);
        outboxEventService.save(outboxEvent);

        return userdetails;
    }
}
