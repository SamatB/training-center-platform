package com.training.paymentservice.mapper;

import com.training.paymentservice.dto.PaymentCreateRequest;
import com.training.paymentservice.dto.PaymentResponse;
import com.training.paymentservice.dto.PaymentUpdateRequest;
import com.training.paymentservice.entity.Payment;
import org.springframework.stereotype.Component;

@Component
public class PaymentMapper {

    public Payment toEntity(PaymentCreateRequest request) {
        return Payment.builder()
                .enrollmentId(request.enrollmentId())
                .userId(request.userId())
                .amount(request.amount())
                .currency(request.currency())
                .build();
    }

    public void updateEntity(Payment payment, PaymentUpdateRequest request) {
        payment.setAmount(request.amount());
        payment.setCurrency(request.currency());
        payment.setStatus(request.status());
    }

    public PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getEnrollmentId(),
                payment.getUserId(),
                payment.getAmount(),
                payment.getCurrency(),
                payment.getStatus(),
                payment.getCreatedAt(),
                payment.getUpdatedAt()
        );
    }
}
