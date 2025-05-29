package com.example.carsharing.dto.payment;

import com.example.carsharing.model.Status;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

public record PaymentCreationRequestDto(
        UUID userId,
        UUID rentalId) {
}
