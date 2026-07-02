package com.training.enrollmentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    @NotNull(message = "userId не может быть null")
    private UUID userId;

    @NotNull(message = "courseId не может быть null")
    private UUID courseId;
}
