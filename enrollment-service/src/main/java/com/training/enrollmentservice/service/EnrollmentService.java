package com.training.enrollmentservice.service;

import com.training.enrollmentservice.client.CourseClient;
import com.training.enrollmentservice.client.UserClient;
import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.entity.Enrollment;
import com.training.enrollmentservice.exception.EnrollmentNotFoundException;
import com.training.enrollmentservice.mapper.EnrollmentMapper;
import com.training.enrollmentservice.repository.EnrollmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);

    private final EnrollmentRepository enrollmentRepository;
    private final EnrollmentMapper enrollmentMapper;
    private final UserClient userClient;
    private final CourseClient courseClient;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, EnrollmentMapper enrollmentMapper, UserClient userClient, CourseClient courseClient) {
        this.enrollmentRepository = enrollmentRepository;
        this.enrollmentMapper = enrollmentMapper;
        this.userClient = userClient;
        this.courseClient = courseClient;
    }

    public EnrollmentResponse createEnrollment(EnrollmentRequest request) {

        logger.info("Отправляем запрос в user-service через OPENFEIGN...");
        userClient.getUserById(request.getUserId());
        logger.info("Получили ответ от user-service через OPENFEIGN...");

        logger.info("Отправляем запрос в course-service через OPENFEIGN...");
        courseClient.getCourseById(request.getCourseId());
        logger.info("Получили ответ от course-service через OPENFEIGN...");

        Enrollment enrollment = enrollmentMapper.toEntity(request);

        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setCreatedAt(LocalDateTime.now());
        enrollment.setUpdatedAt(LocalDateTime.now());
        enrollment.setStatus("ACTIVE");

        Enrollment saved = enrollmentRepository.save(enrollment);
        logger.info("Запись успешно создана!");

        return enrollmentMapper.toResponse(saved);   // ← возвращаем DTO через маппер
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

    public EnrollmentResponse updateEnrollment(UUID id, EnrollmentRequest request) {
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

        enrollmentRepository.deleteById(id);
    }
}