package com.training.documentservice.repository;

import com.training.documentservice.entity.Document;
import com.training.documentservice.entity.DocumentStatus;
import com.training.documentservice.entity.DocumentType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DocumentRepository extends JpaRepository<Document, UUID> {

    List<Document> findByUserId(UUID userId);

    List<Document> findByEnrollmentId(UUID enrollmentId);

    List<Document> findByPaymentId(UUID paymentId);

    List<Document> findByStatus(DocumentStatus status);

    List<Document> findByType(DocumentType type);
}
