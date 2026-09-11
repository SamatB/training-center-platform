package com.training.enrollmentservice.controller;

import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.service.EnrollmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
@Tag(name = "Записи на курсы", description = "Управление записями студентов на курсы")
@Slf4j
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @Operation(summary = "Создать запись на курс")
    @PostMapping
    public EnrollmentResponse createEnrollment(@Valid @RequestBody EnrollmentRequest request) {

        log.info("Получен запрос на создание записи на курс");
        return enrollmentService.createEnrollment(request);
    }

    @Operation(summary = "Получить запись по идентификатору")
    @GetMapping("/{id}")
    public EnrollmentResponse getEnrollmentById(@PathVariable UUID id) {

        log.info("Получен запрос на получение записи с id: {}", id);
        return enrollmentService.getEnrollmentById(id);
    }

    @Operation(summary = "Получить все записи на курсы")
    @GetMapping
    public List<EnrollmentResponse> getAllEnrollments() {

        log.info("Получен запрос на получение всех записей на курсы");
        return enrollmentService.getAllEnrollments();
    }

    @Operation(summary = "Обновить запись на курс")
    @PutMapping("/{id}")
    public EnrollmentResponse updateEnrollment(
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentRequest request) {

        log.info("Получен запрос на обновление записи с id: {}", id);
        return enrollmentService.updateEnrollment(id, request);
    }

    @Operation(summary = "Удалить запись на курс")
    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable UUID id) {
        log.info("Получен запрос на удаление записи с id: {}", id);
        enrollmentService.deleteEnrollment(id);
    }
}
