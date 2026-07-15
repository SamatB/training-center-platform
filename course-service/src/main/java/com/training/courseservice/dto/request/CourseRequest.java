package com.training.courseservice.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

    @NotBlank
    private String teacherName;

    @NotNull
    @Min(1)
    private Integer durationHours;

    @NotNull
    @DecimalMin("0.0")
    private BigDecimal price;

    private Boolean active;
}
