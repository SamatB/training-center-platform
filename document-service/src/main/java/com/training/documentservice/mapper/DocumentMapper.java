package com.training.documentservice.mapper;

import com.training.documentservice.dto.DocumentCreateRequest;
import com.training.documentservice.dto.DocumentResponse;
import com.training.documentservice.dto.DocumentUpdateRequest;
import com.training.documentservice.entity.Document;
import org.springframework.stereotype.Component;

@Component
public class DocumentMapper {

    public Document toEntity(DocumentCreateRequest request) {
        return Document.builder()
                .userId(request.userId())
                .enrollmentId(request.enrollmentId())
                .paymentId(request.paymentId())
                .type(request.type())
                .fileName(request.fileName())
                .storageKey(request.storageKey())
                .build();
    }

    public void updateEntity(Document document, DocumentUpdateRequest request) {
        document.setType(request.type());
        document.setStatus(request.status());
        document.setFileName(request.fileName());
        document.setStorageKey(request.storageKey());
    }

    public DocumentResponse toResponse(Document document) {
        return new DocumentResponse(
                document.getId(),
                document.getUserId(),
                document.getEnrollmentId(),
                document.getPaymentId(),
                document.getType(),
                document.getStatus(),
                document.getFileName(),
                document.getStorageKey(),
                document.getCreatedAt(),
                document.getUpdatedAt()
        );
    }
}
