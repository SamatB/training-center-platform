package com.training.enrollmentservice.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean("userRestClient")
    public RestClient userRestClient(
            @Value("${clients.user-service.url}") String userServiceUrl
    ) {
        return RestClient.builder()
                .baseUrl(userServiceUrl)
                .build();
    }

    @Bean("courseRestClient")
    public RestClient courseRestClient(
            @Value("${clients.course-service.url}") String courseServiceUrl
    ) {
        return RestClient.builder()
                .baseUrl(courseServiceUrl)
                .build();
    }
}