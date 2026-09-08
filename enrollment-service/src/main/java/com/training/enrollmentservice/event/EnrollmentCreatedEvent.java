package com.training.enrollmentservice.event;

import java.util.UUID;

public record EnrollmentCreatedEvent(
        UUID enrollmentId,
        UUID userId,
        UUID courseId
) {
}