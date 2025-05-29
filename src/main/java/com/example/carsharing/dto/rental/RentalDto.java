package com.example.carsharing.dto.rental;

import java.time.LocalDate;
import java.util.UUID;

public record RentalDto(
        UUID id,
        UUID userId,
        UUID carId,
        LocalDate rentalStart,
        LocalDate rentalEnd,
        LocalDate actualReturnDate) {
}
