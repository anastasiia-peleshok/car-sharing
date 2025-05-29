package com.example.carsharing.service;

import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.dto.payment.PaymentResponseDto;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public interface PaymentService {
    PaymentResponseDto createPayment(PaymentCreationRequestDto paymentRequestDto);
    List<PaymentResponseDto> getAllPayments();
    PaymentResponseDto getPaymentById(UUID id);
    void checkPendingPayments();
    void notifyForOverduePayments();
}
