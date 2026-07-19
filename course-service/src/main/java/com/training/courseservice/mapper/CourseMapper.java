package com.training.courseservice.mapper;

import com.training.courseservice.dto.request.CourseRequest;
import com.training.courseservice.dto.response.CourseResponse;
import com.training.courseservice.entity.Course;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public Course toEntity(CourseRequest request) {
        if (request == null) {
            return null;
        }

        return Course.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .teacherName(request.getTeacherName())
                .durationHours(request.getDurationHours())
                .price(request.getPrice())
                .active(request.getActive())
                .build();
    }

    public CourseResponse toResponse(Course course) {
        if (course == null) {
            return null;
        }

        return CourseResponse.builder()
                .id(course.getId())
                .title(course.getTitle())
                .description(course.getDescription())
                .teacherName(course.getTeacherName())
                .durationHours(course.getDurationHours())
                .price(course.getPrice())
                .active(course.getActive())
                .createdAt(course.getCreatedAt())
                .updatedAt(course.getUpdatedAt())
                .build();
    }
}
