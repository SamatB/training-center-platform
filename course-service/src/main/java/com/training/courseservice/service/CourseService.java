package com.training.courseservice.service;

import com.training.courseservice.dto.request.CourseRequest;
import com.training.courseservice.dto.response.CourseResponse;
import com.training.courseservice.entity.Course;
import com.training.courseservice.exception.CourseNotFoundException;
import com.training.courseservice.mapper.CourseMapper;
import com.training.courseservice.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseMapper courseMapper;

    public CourseResponse createCourse(CourseRequest request) {
        log.info("Создание курса: {}", request.getTitle());

        Course course = courseMapper.toEntity(request);

        Course savedCourse = courseRepository.save(course);

        log.info("Курс успешно создан с id: {}", savedCourse.getId());

        return courseMapper.toResponse(savedCourse);
    }

    public CourseResponse getCourseById(UUID id) {
        log.info("Поиск курса с id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Курс с id: {} не найден", id);

                    return new CourseNotFoundException(
                            "Курс с идентификатором "
                                    + id
                                    + " не найден"
                    );
                });

        log.info("Курс с id: {} успешно найден", id);

        return courseMapper.toResponse(course);
    }

    public List<CourseResponse> getAllCourses() {
        log.info("Получение списка всех курсов");

        List<CourseResponse> courses =
                courseRepository.findAll()
                        .stream()
                        .map(courseMapper::toResponse)
                        .toList();

        log.info("Получено курсов: {}", courses.size());

        return courses;
    }

    public List<CourseResponse> getCoursesByTeacherId(
            UUID teacherId
    ) {
        log.info("Получение курсов преподавателя с teacherId: {}", teacherId);

        List<CourseResponse> courses =
                courseRepository.findByTeacherId(teacherId)
                        .stream()
                        .map(courseMapper::toResponse)
                        .toList();

        log.info("Для преподавателя с teacherId: {} найдено курсов: {}", teacherId, courses.size());

        return courses;
    }

    public CourseResponse updateCourse(
            UUID id,
            CourseRequest request
    ) {
        log.info("Обновление курса с id: {}", id);

        Course course = courseRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Курс с id: {} не найден", id);

                    return new CourseNotFoundException(
                            "Курс с идентификатором "
                                    + id
                                    + " не найден"
                    );
                });

        course.setTitle(request.getTitle());
        course.setDescription(request.getDescription());
        course.setTeacherId(request.getTeacherId());
        course.setTeacherName(request.getTeacherName());
        course.setDurationHours(request.getDurationHours());
        course.setPrice(request.getPrice());
        course.setActive(request.getActive());
        course.setUpdatedAt(LocalDateTime.now());

        Course updatedCourse =
                courseRepository.save(course);

        log.info("Курс с id: {} успешно обновлён", id);

        return courseMapper.toResponse(updatedCourse);
    }

    public void deleteCourse(UUID id) {
        log.info("Удаление курса с id: {}", id);

        if (!courseRepository.existsById(id)) {
            log.warn("Курс с id: {} не найден", id);

            throw new CourseNotFoundException(
                    "Курс с идентификатором "
                            + id
                            + " не найден"
            );
        }

        courseRepository.deleteById(id);

        log.info("Курс с id: {} успешно удалён", id);
    }
}