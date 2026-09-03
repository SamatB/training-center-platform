package com.training.authservice.dto.response;

import com.training.authservice.entity.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Response containing user account information")
public class UserAccountResponse {

    @Schema(
            description = "Unique identifier of the user",
            example = "550e8400-e29b-41d4-a716-446655440000"
    )
    private UUID id;

    @Schema(
            description = "User's first name",
            example = "John"
    )
    private String firstName;

    @Schema(
            description = "User's last name",
            example = "Doe"
    )
    private String lastName;

    @Schema(
            description = "User's email address",
            example = "john.doe@gmail.com"
    )
    private String email;

    @Schema(
            description = "User's role",
            example = "STUDENT"
    )
    private Role role;

    @Schema(
            description = "Indicates whether the user account is enabled",
            example = "true"
    )
    private Boolean enabled;

    @Schema(
            description = "Date and time when the account was created",
            example = "2026-09-03T21:30:00"
    )
    private LocalDateTime createdAt;

    @Schema(
            description = "Date and time when the account was last updated",
            example = "2026-09-03T21:30:00"
    )
    private LocalDateTime updatedAt;
}