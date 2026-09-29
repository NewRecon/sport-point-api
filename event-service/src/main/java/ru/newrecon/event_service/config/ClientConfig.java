package ru.newrecon.event_service.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration 
public class ClientConfig {

    @Bean
    public RestClient subscriptionRestClient() {
        return RestClient.create("http://localhost:8084/api/v1/subscriptions");
    }

    @Bean
    public RestClient profileRestClient() {
        return RestClient.create("http://localhost:8083/api/v1/profiles");
    }
}
