package com.training.notificationservice.event;

import java.util.UUID;

public record UserRegisteredEvent(
        UUID userId,
        String firstName,
        String lastName,
        String email
) {
}