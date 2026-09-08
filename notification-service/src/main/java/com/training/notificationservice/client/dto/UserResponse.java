package com.training.notificationservice.client.dto;

import java.util.UUID;

public record UserResponse(
        UUID id,
        String email
) {
}