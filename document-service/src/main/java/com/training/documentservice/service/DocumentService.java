package com.training.documentservice.service;

import com.training.documentservice.dto.DocumentCreateRequest;
import com.training.documentservice.dto.DocumentResponse;
import com.training.documentservice.dto.DocumentUpdateRequest;
import com.training.documentservice.entity.Document;
import com.training.documentservice.mapper.DocumentMapper;
import com.training.documentservice.repository.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class DocumentService {

    private final DocumentRepository documentRepository;
    private final DocumentMapper documentMapper;

    @Transactional
    public DocumentResponse create(DocumentCreateRequest request) {
        log.info(
                "Создание документа для userId={} enrollmentId={} paymentId={} type={}",
                request.userId(),
                request.enrollmentId(),
                request.paymentId(),
                request.type()
        );

        Document document = documentMapper.toEntity(request);
        Document saved = documentRepository.save(document);

        log.info("Документ создан successfully with id={}", saved.getId());
        return documentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public DocumentResponse getById(UUID id) {
        log.info("Получение документа по id={}", id);
        return documentMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getByUserId(UUID userId) {
        return documentRepository.findByUserId(userId)
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<DocumentResponse> getAll() {
        log.info("Получение всех документов");
        return documentRepository.findAll()
                .stream()
                .map(documentMapper::toResponse)
                .toList();
    }

    @Transactional
    public DocumentResponse update(UUID id, DocumentUpdateRequest request) {
        log.info("Обновление документа с id={}", id);

        Document document = findById(id);
        documentMapper.updateEntity(document, request);
        Document saved = documentRepository.save(document);

        log.info("Документ обновлён successfully with id={}", saved.getId());
        return documentMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Удаление документа с id={}", id);

        Document document = findById(id);
        documentRepository.delete(document);

        log.info("Документ удалён successfully with id={}", id);
    }

    private Document findById(UUID id) {
        return documentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Документ не найден with id: " + id
                ));
    }
}
