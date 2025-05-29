package com.example.carsharing.dto.payment;

import com.example.carsharing.model.Status;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record PaymentResponseDto(
        BigDecimal amount,
        Status status,
        UUID userId,
        UUID rentalId,
        LocalDateTime paymentTime,
        String paymentLink) {
}
