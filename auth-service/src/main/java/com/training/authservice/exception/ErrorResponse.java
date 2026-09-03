package com.training.authservice.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Standard error response")
public class ErrorResponse {

    @Schema(
            description = "Date and time when the error occurred",
            example = "2026-09-03T21:30:00"
    )
    private LocalDateTime timestamp;

    @Schema(
            description = "HTTP status code",
            example = "400"
    )
    private int status;

    @Schema(
            description = "HTTP error description",
            example = "Bad Request"
    )
    private String error;

    @Schema(
            description = "Detailed error message",
            example = "email: Требуется электронная почта"
    )
    private String message;

    @Schema(
            description = "Request path",
            example = "/api/auth/register"
    )
    private String path;
}