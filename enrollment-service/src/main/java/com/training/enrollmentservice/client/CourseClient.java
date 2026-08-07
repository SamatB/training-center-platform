package com.training.enrollmentservice.client;

import com.training.enrollmentservice.client.dto.CourseResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.UUID;

@Component
public class CourseClient {

    private final RestClient restClient;

    public CourseClient(@Qualifier("courseRestClient") RestClient restClient) {
        this.restClient = restClient;
    }

    public CourseResponse getCourseById(UUID courseId) {
        return restClient.get()
                .uri("/api/v1/courses/{id}", courseId)
                .retrieve()
                .body(CourseResponse.class);
    }
}