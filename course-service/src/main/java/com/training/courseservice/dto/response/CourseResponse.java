package com.training.courseservice.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
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
