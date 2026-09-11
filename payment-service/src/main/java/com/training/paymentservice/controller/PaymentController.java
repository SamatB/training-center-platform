package com.training.paymentservice.controller;

import com.training.paymentservice.dto.PaymentCreateRequest;
import com.training.paymentservice.dto.PaymentResponse;
import com.training.paymentservice.dto.PaymentUpdateRequest;
import com.training.paymentservice.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@Slf4j
@Tag(
        name = "Платежи",
        description = "Операции создания, получения, обновления и удаления платежей"
)
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Создать платеж")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Платеж успешно создан"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            )
    })
    public PaymentResponse create(
            @Valid @RequestBody PaymentCreateRequest request
    ) {
        log.info(
                "Получен запрос на создание платежа для enrollmentId={} userId={}",
                request.enrollmentId(),
                request.userId()
        );

        return paymentService.create(request);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Получить платеж по ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Платеж найден"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Платеж не найден"
            )
    })
    public PaymentResponse getById(
            @PathVariable UUID id
    ) {
        log.info(
                "Получен запрос на получение платежа с id={}",
                id
        );

        return paymentService.getById(id);
    }

    @GetMapping
    @Operation(summary = "Получить все платежи")
    public List<PaymentResponse> getAll() {
        log.info(
                "Получен запрос на получение всех платежей"
        );

        return paymentService.getAll();
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Получить платежи пользователя")
    public List<PaymentResponse> getByUserId(
            @PathVariable UUID userId
    ) {
        log.info(
                "Получен запрос на получение платежей пользователя с userId={}",
                userId
        );

        return paymentService.getByUserId(userId);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Обновить платеж")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Платеж успешно обновлён"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Некорректные данные запроса"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Платеж не найден"
            )
    })
    public PaymentResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody PaymentUpdateRequest request
    ) {
        log.info(
                "Получен запрос на обновление платежа с id={}",
                id
        );

        return paymentService.update(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Удалить платеж")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Платеж успешно удалён"
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Платеж не найден"
            )
    })
    public void delete(
            @PathVariable UUID id
    ) {
        log.info(
                "Получен запрос на удаление платежа с id={}",
                id
        );

        paymentService.delete(id);
    }
}