package com.training.authservice.dto.response;

import com.training.authservice.entity.Role;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserSummaryResponse(
        UUID id,
        String email,
        String firstName,
        String lastName,
        Role role,
        boolean enabled,
        LocalDateTime createdAt
) {
}
