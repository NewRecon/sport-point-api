package ru.newrecon.profile_service.kafka.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.profile_service.kafka.payload.CreateUserPayload;
import ru.newrecon.profile_service.service.ProfileService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserConsumer {

    private final ObjectMapper kafkObjectMapper;
    private final ProfileService profileService;

    @KafkaListener(topics = "create-user-events")
    public void listenCreateUser(String message) {
        log.info("Получено сообщение из create-user-events : " + message);
        CreateUserPayload createUserDto = kafkObjectMapper.readValue(message, CreateUserPayload.class);
        profileService.create(createUserDto);
    }
}
