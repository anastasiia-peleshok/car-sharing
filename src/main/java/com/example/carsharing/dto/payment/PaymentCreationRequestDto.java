package com.example.carsharing.dto.payment;

import java.util.UUID;

public record PaymentCreationRequestDto(
        UUID userId,
        UUID rentalId) {
}
