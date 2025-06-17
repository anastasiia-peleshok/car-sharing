package com.example.carsharing.dto.car;

import com.example.carsharing.dto.feature.FeatureDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.model.CarType;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

public record FullCarDto(
        UUID id,
        String brand,
        String model,
        String color,
        CarType carType,
        int year,
        BigDecimal price,
        boolean isAvailable,
        Set<FeatureDto> features,
        Set<RentalDto> rentals) {
}
