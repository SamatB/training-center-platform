package com.training.courseservice.repository;

import com.training.courseservice.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CourseRepository extends JpaRepository<Course, UUID> {

    Optional<Course> findByTitle(String title);

    boolean existsByTitle(String title);

    List<Course> findByTitleContainingIgnoreCase(String title);
}
