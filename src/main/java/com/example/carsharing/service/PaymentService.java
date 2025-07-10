package com.example.carsharing.service;

import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.model.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto createPayment(UUID rentalId);

    Page<PaymentResponseDto> getAllPayments(Pageable pageable);

    PaymentResponseDto getPaymentById(UUID id);

    void checkPendingPayments();

    void notifyForOverduePayments();

    Page<PaymentResponseDto> getPaymentByStatus(Status status, Pageable  pageable);
}
