package ru.newrecon.profile_service.consumer;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.newrecon.profile_service.dto.kafka.CreateUserDto;
import ru.newrecon.profile_service.service.ProfileService;
import tools.jackson.databind.ObjectMapper;

@Slf4j
@Component
@RequiredArgsConstructor
public class ProfileConsumer {

    private final ObjectMapper kafkObjectMapper;
    private final ProfileService profileService;

    @KafkaListener(topics = "create-user-events")
    public void listenCreateUser(String message) {
        log.info("Recieve message from create-user-events : " + message);
        CreateUserDto createUserDto = kafkObjectMapper.readValue(message, CreateUserDto.class);
        profileService.create(createUserDto);
    }
}
