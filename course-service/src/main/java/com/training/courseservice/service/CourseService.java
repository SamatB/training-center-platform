package com.training.courseservice.service;

import com.training.courseservice.dto.request.CourseRequest;
import com.training.courseservice.dto.response.CourseResponse;
import com.training.courseservice.entity.Course;
import com.training.courseservice.mapper.CourseMapper;
import com.training.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseResponse createCourse(CourseRequest request) {
        Course course = courseMapper.toEntity(request);
        Course savedCourse = courseRepository.save(course);
        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse getCourseById(UUID id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Курс с таким идентификатором не найден: " + id));

        return courseMapper.toResponse(course);
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(courseMapper::toResponse)
                .toList();
    }

    public CourseResponse updateCourse(UUID id, CourseRequest request) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Курс с таким идентификатором не найден: " + id));

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacherName(request.getTeacherName());
        course.setDurationHours(request.getDurationHours());
        course.setPrice(request.getPrice());
        course.setActive(request.getActive());

        Course updatedCourse = courseRepository.save(course);

        return courseMapper.toResponse(updatedCourse);
    }

    public void deleteCourse(UUID id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Курс с таким идентификатором не найден: " + id);
        }

        courseRepository.deleteById(id);
    }
}
