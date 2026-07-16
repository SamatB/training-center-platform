package com.training.enrollmentservice.controller;

import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.service.EnrollmentService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping
    public EnrollmentResponse createEnrollment(@Valid @RequestBody EnrollmentRequest request) {

        return enrollmentService.createEnrollment(request);
    }

    @GetMapping("/{id}")
    public EnrollmentResponse getEnrollmentById(@PathVariable UUID id) {

        return enrollmentService.getEnrollmentById(id);
    }

    @GetMapping
    public List<EnrollmentResponse> getAllEnrollments() {

        return enrollmentService.getAllEnrollments();
    }

    @PutMapping("/{id}")
    public EnrollmentResponse updateEnrollment(
            @PathVariable UUID id,
            @Valid @RequestBody EnrollmentRequest request) {

        return enrollmentService.updateEnrollment(id, request);
    }

    @DeleteMapping("/{id}")
    public void deleteEnrollment(@PathVariable UUID id) {
        enrollmentService.deleteEnrollment(id);
    }
}
