package com.training.paymentservice.service;

import com.training.paymentservice.dto.PaymentCreateRequest;
import com.training.paymentservice.dto.PaymentResponse;
import com.training.paymentservice.dto.PaymentUpdateRequest;
import com.training.paymentservice.entity.Payment;
import com.training.paymentservice.exception.PaymentAlreadyExistsException;
import com.training.paymentservice.mapper.PaymentMapper;
import com.training.paymentservice.repository.PaymentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;

    @Transactional
    public PaymentResponse create(PaymentCreateRequest request) {
        log.info(
                "Создание платежа для enrollmentId={} userId={} amount={}",
                request.enrollmentId(),
                request.userId(),
                request.amount()
        );

        if (paymentRepository.existsByEnrollmentId(request.enrollmentId())) {
            log.warn(
                    "Платёж для enrollmentId={} уже существует",
                    request.enrollmentId()
            );

            throw new PaymentAlreadyExistsException(
                    "Платёж для этой записи на курс уже существует"
            );
        }

        Payment payment = paymentMapper.toEntity(request);

        Payment saved = paymentRepository.save(payment);

        log.info(
                "Платёж успешно создан: id={} enrollmentId={}",
                saved.getId(),
                saved.getEnrollmentId()
        );

        return paymentMapper.toResponse(saved);
    }

    @Transactional(readOnly = true)
    public PaymentResponse getById(UUID id) {
        log.info("Получение платежа по id={}", id);

        return paymentMapper.toResponse(findById(id));
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getAll() {
        log.info("Получение всех платежей");

        return paymentRepository.findAll()
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PaymentResponse> getByUserId(UUID userId) {
        log.info(
                "Получение платежей пользователя с userId={}",
                userId
        );

        return paymentRepository.findByUserId(userId)
                .stream()
                .map(paymentMapper::toResponse)
                .toList();
    }

    @Transactional
    public PaymentResponse update(
            UUID id,
            PaymentUpdateRequest request
    ) {
        log.info("Обновление платежа с id={}", id);

        Payment payment = findById(id);

        paymentMapper.updateEntity(payment, request);

        Payment saved = paymentRepository.save(payment);

        log.info(
                "Платеж успешно обновлён with id={}",
                saved.getId()
        );

        return paymentMapper.toResponse(saved);
    }

    @Transactional
    public void delete(UUID id) {
        log.info("Удаление платежа с id={}", id);

        Payment payment = findById(id);

        paymentRepository.delete(payment);

        log.info("Платеж успешно удалён with id={}", id);
    }

    private Payment findById(UUID id) {
        return paymentRepository.findById(id)
                .orElseThrow(() ->
                        new EntityNotFoundException(
                                "Платеж не найден with id: " + id
                        )
                );
    }
}