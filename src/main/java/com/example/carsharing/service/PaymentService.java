package com.example.carsharing.service;

import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.dto.payment.PaymentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentCreationRequestDto paymentRequestDto);

    Page<PaymentResponseDto> getAllPayments(Pageable pageable);

    PaymentResponseDto getPaymentById(UUID id);

    void checkPendingPayments();

    void notifyForOverduePayments();
}
