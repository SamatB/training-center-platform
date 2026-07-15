package com.training.enrollmentservice.repository;

import com.training.enrollmentservice.entity.Enrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;
@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, UUID> {

    List<Enrollment> findByUserId(UUID userId);

    List<Enrollment> findByCourseId(UUID courseId);

    boolean existsByUserIdAndCourseId(UUID userId, UUID courseId);
}
