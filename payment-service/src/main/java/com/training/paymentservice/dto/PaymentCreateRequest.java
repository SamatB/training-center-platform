package com.training.paymentservice.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCreateRequest(
        @NotNull(message = "ID записи на курс не должен быть пустым")
        UUID enrollmentId,

        @NotNull(message = "ID пользователя не должен быть пустым")
        UUID userId,

        @NotNull(message = "Сумма не должна быть пустой")
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
        BigDecimal amount,

        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Код валюты должен состоять ровно из 3 заглавных букв"
        )
        String currency
) {
}
