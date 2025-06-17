package com.example.carsharing.dto.rental;

import com.example.carsharing.dto.car.CarDto;

import java.time.LocalDateTime;
import java.util.UUID;

public record RentalWithDetailedCarInfoDto(
        UUID id,
        UUID userId,
        CarDto car,
        LocalDateTime rentalStart,
        LocalDateTime rentalEnd,
        LocalDateTime actualReturnDate) {
}
