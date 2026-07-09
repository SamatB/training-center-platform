package com.training.enrollmentservice.service;

import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.entity.Enrollment;
import com.training.enrollmentservice.mapper.EnrollmentMapper;
import com.training.enrollmentservice.repository.EnrollmentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
    }

    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {
        Enrollment enrollment = enrollmentMapper.toEntity(request);

        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        enrollment.setStatus("ACTIVE");

        Enrollment saved = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponse(saved);   // ← возвращаем DTO через маппер
    }

    public EnrollmentResponse getEnrollmentById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Запись не найдена с id: " + id
                        ));

        return enrollmentMapper.toResponse(enrollment);
    }

    public List<EnrollmentResponse> getAllEnrollments() {
        return enrollmentRepository.findAll()
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public EnrollmentResponse updateEnrollment(UUID id, EnrollmentRequest request) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Запись не найдена с id: " + id
                        ));

        enrollment.setUserId(request.getUserId());
        enrollment.setCourseId(request.getCourseId());
        enrollment.setUpdatedAt(LocalDateTime.now());

        Enrollment updated = enrollmentRepository.save(enrollment);

        return enrollmentMapper.toResponse(updated);
    }

    public void deleteEnrollment(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Запись не найдена с id: " + id));
        enrollmentRepository.deleteById(id);
    }
}