package com.training.enrollmentservice.client;
import org.springframework.cloud.openfeign.FeignClient;
import com.training.enrollmentservice.dto.response.UserResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.UUID;

@FeignClient(name = "user-service")
public interface UserClient {
    @GetMapping("/api/v1/users/{id}")
    UserResponse getById(@PathVariable("id")UUID id);




    }

