package com.example.carsharing.dto.rental;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentalDto(
        UUID id,
        UUID userId,
        UUID carId,
        LocalDateTime rentalStart,
        LocalDateTime rentalEnd,
        LocalDateTime actualReturnDate,
        boolean isReturned) {
}
