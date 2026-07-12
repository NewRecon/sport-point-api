package ru.newrecon.subscription_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfig {

    @Bean
    public ObjectMapper kafkObjectMapper() {
        return new ObjectMapper();
    }
}
