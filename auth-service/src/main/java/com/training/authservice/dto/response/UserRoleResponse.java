package com.training.authservice.dto.response;

import com.training.authservice.entity.Role;

import java.util.UUID;

public record UserRoleResponse(
        UUID id,
        String email,
        Role role
) {
}
