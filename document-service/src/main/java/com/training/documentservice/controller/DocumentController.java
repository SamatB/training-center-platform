package com.training.documentservice.controller;

import com.training.documentservice.dto.DocumentCreateRequest;
import com.training.documentservice.dto.DocumentResponse;
import com.training.documentservice.dto.DocumentUpdateRequest;
import com.training.documentservice.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/documents")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Документы",
        description = "Операции управления документами"
)
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать документ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Документ создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public DocumentResponse create(
            @Valid @RequestBody DocumentCreateRequest request
    ) {
        log.info(
                "Получен запрос на создание документа для userId={} type={}",
                request.userId(),
                request.type()
        );
        return documentService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить документ по ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Документ найден"),
            @ApiResponse(responseCode = "404", description = "Документ не найден")
    })
    public DocumentResponse getById(@PathVariable UUID id) {
        log.info("Получен запрос на получение документа с id={}", id);
        return documentService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получить все документы")
    public List<DocumentResponse> getAll() {
        log.info("Получен запрос на получение всех документов");
        return documentService.getAll();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить документ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Документ обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Документ не найден")
    })
    public DocumentResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody DocumentUpdateRequest request
    ) {
        log.info("Получен запрос на обновление документа с id={}", id);
        return documentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить документ")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Документ удалён"),
            @ApiResponse(responseCode = "404", description = "Документ не найден")
    })
    public void delete(@PathVariable UUID id) {
        log.info("Получен запрос на удаление документа с id={}", id);
        documentService.delete(id);
    }
}
