package com.training.enrollmentservice.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {

    @NotNull(message = "Идентификатор пользователя не может быть пустым")
    private UUID userId;

    @NotNull(message = "Идентификатор курса не может быть пустым")
    private UUID courseId;
}