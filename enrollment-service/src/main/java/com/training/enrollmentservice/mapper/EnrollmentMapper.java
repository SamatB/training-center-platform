package com.training.enrollmentservice.mapper;

import com.training.enrollmentservice.dto.request.EnrollmentRequest;
import com.training.enrollmentservice.dto.response.EnrollmentResponse;
import com.training.enrollmentservice.entity.Enrollment;
import org.springframework.stereotype.Component;

@Component
public class EnrollmentMapper {

    public Enrollment toEntity(EnrollmentRequest request) {
        Enrollment enrollment = new Enrollment();
        enrollment.setUserId(request.getUserId());
        enrollment.setCourseId(request.getCourseId());

        return enrollment;
    }

    public EnrollmentResponse toResponse(Enrollment enrollment) {
        EnrollmentResponse response = new EnrollmentResponse();

        response.setId(enrollment.getId());
        response.setUserId(enrollment.getUserId());
        response.setCourseId(enrollment.getCourseId());
        response.setStatus(enrollment.getStatus());
        response.setEnrollmentDate(enrollment.getEnrollmentDate());
        response.setCreatedAt(enrollment.getCreatedAt());
        response.setUpdatedAt(enrollment.getUpdatedAt());

        return response;
    }
}
