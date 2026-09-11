package com.training.courseservice.controller;

import com.training.courseservice.dto.request.CourseRequest;
import com.training.courseservice.dto.response.CourseResponse;
import com.training.courseservice.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/v1/courses")
@RequiredArgsConstructor
@Tag(name = "Курсы", description = "Операции для работы с курсами")
public class CourseController {

    private final CourseService courseService;

    @PostMapping
    @Operation(summary = "Создать курс", description = "Создаёт новый курс")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно создан"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса")
    })
    public CourseResponse createCourse(@Valid @RequestBody CourseRequest request) {
        log.info("Получен запрос на создание курса: {}", request.getTitle());
        return courseService.createCourse(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить курс по ID", description = "Возвращает курс по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс найден"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public CourseResponse getCourseById(@PathVariable UUID id) {
        log.info("Получен запрос на получение курса с id: {}", id);
        return courseService.getCourseById(id);
    }

    @GetMapping
    @Operation(summary = "Получить список курсов", description = "Возвращает список всех курсов")
    @ApiResponse(responseCode = "200", description = "Список курсов успешно получен")
    public List<CourseResponse> getAllCourses() {
        log.info("Получен запрос на получение списка курсов");
        return courseService.getAllCourses();
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить курс", description = "Обновляет данные существующего курса")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно обновлён"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные запроса"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public CourseResponse updateCourse(@PathVariable UUID id,
                                       @Valid @RequestBody CourseRequest request) {
        log.info("Получен запрос на обновление курса с id: {}", id);
        return courseService.updateCourse(id, request);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Удалить курс", description = "Удаляет курс по его идентификатору")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Курс успешно удалён"),
            @ApiResponse(responseCode = "404", description = "Курс не найден")
    })
    public void deleteCourse(@PathVariable UUID id) {
        log.info("Получен запрос на удаление курса с id: {}", id);
        courseService.deleteCourse(id);
    }
}
