package com.training.authservice.dto.request;

import com.training.authservice.entity.Role;
import jakarta.validation.constraints.NotNull;

public record ChangeUserRoleRequest(
        @NotNull(message = "Role must not be null")
        Role role
) {
}
