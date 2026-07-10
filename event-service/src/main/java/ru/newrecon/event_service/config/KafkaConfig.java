package ru.newrecon.event_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import tools.jackson.databind.ObjectMapper;

@Configuration
public class KafkaConfig {

    @Bean
    public NewTopic eventCreateEventsTopic() {
        return TopicBuilder.name("create-event-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public NewTopic eventDeleteEventsTopic() {
        return TopicBuilder.name("delete-event-events")
                .partitions(1)
                .replicas(1)
                .build();
    }

    @Bean
    public ObjectMapper kafkObjectMapper() {
        return new ObjectMapper();
    }
}
