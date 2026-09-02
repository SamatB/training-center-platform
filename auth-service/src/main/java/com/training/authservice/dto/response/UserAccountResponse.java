package com.training.authservice.dto.response;

import com.training.authservice.entity.Role;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAccountResponse {

    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private Role role;
    private Boolean enabled;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}