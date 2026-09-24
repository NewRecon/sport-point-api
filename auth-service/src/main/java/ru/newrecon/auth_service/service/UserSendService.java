package ru.newrecon.auth_service.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import ru.newrecon.auth_service.dto.kafka.CreateUserDto;
import ru.newrecon.auth_service.entity.User;
import ru.newrecon.auth_service.producer.UserProducer;
import tools.jackson.databind.ObjectMapper;

@Service
@RequiredArgsConstructor
public class UserSendService {

    private final UserProducer userProducer;
    private final ObjectMapper kafkObjectMapper;

    public void sendCreate(User user) {

        CreateUserDto createUserDto = new CreateUserDto(user.getId(), user.getUsername());

        String kafkaMessage = kafkObjectMapper.writeValueAsString(createUserDto);
        userProducer.sendCreate(kafkaMessage);
    }
}
