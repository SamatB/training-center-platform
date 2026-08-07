package com.training.enrollmentservice.client.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private UUID id;
    private String title;
    private String description;
    private String teacherName;
    private Integer durationHours;
    private BigDecimal price;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}