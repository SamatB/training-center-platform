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

    @NotBlank(message = "Название курса обязательно")
    private String title;

    @NotBlank(message = "Описание курса обязательно")
    private String description;

    @NotBlank(message = "Имя преподавателя обязательно")
    private String teacherName;

    @NotNull(message = "Продолжительность обязательна")
    @Min(value = 1, message = "Продолжительность должна быть больше 0")
    private Integer durationHours;

    @NotNull(message = "Цена обязательна")
    @DecimalMin(value = "0.0", inclusive = true, message = "Цена не может быть отрицательной")
    private BigDecimal price;

    private Boolean active;
}
