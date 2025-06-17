package com.example.carsharing.dto.rental;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Positive;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentalCreationRequestDto(
        @Positive
        UUID carId,
        @Future
        @JsonFormat(pattern = "yyyy-MM-dd")
        LocalDateTime rentalEnd) {
}
