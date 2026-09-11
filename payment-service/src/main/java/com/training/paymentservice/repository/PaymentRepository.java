package com.training.paymentservice.repository;

import com.training.paymentservice.entity.Payment;
import com.training.paymentservice.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {

    List<Payment> findByUserId(UUID userId);

    List<Payment> findByEnrollmentId(UUID enrollmentId);

    List<Payment> findByStatus(PaymentStatus status);
}
