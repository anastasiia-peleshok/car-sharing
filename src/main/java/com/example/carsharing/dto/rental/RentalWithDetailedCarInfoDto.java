package com.example.carsharing.dto.rental;

import com.example.carsharing.dto.car.CarDto;

import java.time.LocalDate;
import java.util.UUID;

public record RentalWithDetailedCarInfoDto(
        UUID id,
        UUID userId,
        CarDto car,
        LocalDate rentalStart,
        LocalDate rentalEnd,
        LocalDate actualReturnDate) {
}
