package com.training.notificationservice.controller;

import com.training.notificationservice.dto.NotificationCreateRequest;
import com.training.notificationservice.dto.NotificationResponse;
import com.training.notificationservice.service.NotificationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import com.training.notificationservice.dto.EmailRequest;

import java.util.UUID;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
@Tag(
        name = "Уведомления",
        description = "Операции создания, получения и отправки уведомлений"
)
public class NotificationController {

    private final NotificationService notificationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "Создать уведомление",
            description = "Создаёт новое уведомление со статусом PENDING"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Уведомление успешно создано"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            )
    })
    public NotificationResponse create(
            @Valid @RequestBody NotificationCreateRequest request
    ) {
        return notificationService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Получить уведомление по ID",
            description = "Возвращает данные уведомления по его UUID"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Уведомление успешно найдено"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректный UUID уведомления"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Уведомление не найдено"
            )
    })
    public NotificationResponse getById(@PathVariable UUID id) {

        return notificationService.getById(id);
    }
    @PostMapping("/email")
    @ResponseStatus(HttpStatus.OK)
    @Operation(
            summary = "Отправить Email",
            description = "Отправляет уведомление на электронную почту"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Email успешно отправлен"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Не удалось отправить Email"
            )
    })
    public void sendEmail(
            @Valid @RequestBody EmailRequest request
    ) {
        notificationService.sendEmail(
                request.to(),
                request.subject(),
                request.message()
        );
    }

}
