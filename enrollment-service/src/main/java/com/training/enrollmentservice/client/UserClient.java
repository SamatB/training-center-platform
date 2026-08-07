package com.training.enrollmentservice.client;

import com.training.enrollmentservice.client.dto.UserResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class UserClient {

    private final RestClient restClient;

    public UserClient(
            @Qualifier("userRestClient") RestClient restClient
    ) {
        this.restClient = restClient;
    }

    public UserResponse getUserById(UUID userId) {
        return restClient.get()
                .uri("/api/v1/users/{id}", userId)
                .retrieve()
                .body(UserResponse.class);
    }
}