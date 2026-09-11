package com.training.authservice.dto.response;

import java.util.UUID;

public record UserStatusResponse(
        UUID id,
        String email,
        boolean enabled
) {
}
