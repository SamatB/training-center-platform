package com.training.documentservice.dto;

import com.training.documentservice.entity.DocumentStatus;
import com.training.documentservice.entity.DocumentType;

import java.time.LocalDateTime;
import java.util.UUID;

public record DocumentResponse(
        UUID id,
        UUID userId,
        UUID enrollmentId,
        UUID paymentId,
        DocumentType type,
        DocumentStatus status,
        String fileName,
        String storageKey,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
