package com.training.paymentservice.dto;

import com.training.paymentservice.entity.PaymentStatus;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

import java.math.BigDecimal;

public record PaymentUpdateRequest(
        @NotNull(message = "Сумма не должна быть пустой")
        @DecimalMin(value = "0.01", message = "Сумма должна быть больше нуля")
        BigDecimal amount,

        @NotNull(message = "Валюта не должна быть пустой")
        @Pattern(
                regexp = "^[A-Z]{3}$",
                message = "Код валюты должен состоять ровно из 3 заглавных букв"
        )
        String currency,

        @NotNull(message = "Статус платежа не должен быть пустым")
        PaymentStatus status
) {
}
