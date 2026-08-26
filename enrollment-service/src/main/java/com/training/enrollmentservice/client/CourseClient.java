package com.training.enrollmentservice.client;
import org.springframework.cloud.openfeign.FeignClient;

@FeignClient(name = "course-service")
public interface CourseClient {
}
