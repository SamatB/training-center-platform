package com.training.authservice.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Authentication response with JWT Access Token")
public record AuthResponse(
        @Schema(description = "JWT Access Token")
        String accessToken,
        @Schema(example = "Bearer")
        String tokenType
) {
}
