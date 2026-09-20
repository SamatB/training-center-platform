package com.training.enrollmentservice.client;
import com.training.enrollmentservice.dto.response.CourseResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import java.util.UUID;
@FeignClient(name = "course-service")
public interface CourseClient {

    @GetMapping("/api/v1/courses/{id}")
    CourseResponse getCourseById(@PathVariable("id") UUID id);
}