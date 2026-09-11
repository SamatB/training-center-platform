package com.training.enrollmentservice.service;

import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.entity.Enrollment;
import com.training.enrollmentservice.event.EnrollmentCreatedEvent;
import com.training.enrollmentservice.exception.EnrollmentAlreadyExistsException;
import com.training.enrollmentservice.exception.EnrollmentNotFoundException;
import com.training.enrollmentservice.kafka.EnrollmentEventProducer;
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
    private final EnrollmentEventProducer enrollmentEventProducer;

    public EnrollmentService(
            EnrollmentRepository enrollmentRepository,
            EnrollmentMapper enrollmentMapper,
            EnrollmentEventProducer enrollmentEventProducer
    ) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.enrollmentEventProducer = enrollmentEventProducer;
    }

    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {

        boolean alreadyExists =
                enrollmentRepository.existsByUserIdAndCourseId(
                        request.getUserId(),
                        request.getCourseId()
                );

        if (alreadyExists) {
            throw new EnrollmentAlreadyExistsException(
                    "Вы уже записаны на этот курс"
            );
        }
        Enrollment enrollment = enrollmentMapper.toEntity(request);

        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        enrollment.setStatus("ACTIVE");

        Enrollment saved = enrollmentRepository.save(enrollment);

        EnrollmentCreatedEvent event = new EnrollmentCreatedEvent(
                saved.getId(),
                saved.getUserId(),
                saved.getCourseId()
        );

        enrollmentEventProducer.sendEnrollmentCreatedEvent(event);

        return enrollmentMapper.toResponse(saved);
    }

    public EnrollmentResponse getEnrollmentById(UUID id) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
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

    public List<EnrollmentResponse> getEnrollmentsByUserId(UUID userId) {
        return enrollmentRepository.findByUserId(userId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public List<EnrollmentResponse> getEnrollmentsByCourseId(UUID courseId) {
        return enrollmentRepository.findByCourseId(courseId)
                .stream()
                .map(enrollmentMapper::toResponse)
                .toList();
    }

    public EnrollmentResponse updateEnrollment(
            UUID id,
            EnrollmentRequest request
    ) {
        Enrollment enrollment = enrollmentRepository.findById(id)
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
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
                .orElseThrow(() ->
                        new EnrollmentNotFoundException(
                                "Запись не найдена с id: " + id
                        ));

        enrollmentRepository.deleteById(enrollment.getId());
    }
}