package com.example.carsharing.dto.car;

import com.example.carsharing.model.CarType;

import java.math.BigDecimal;
import java.util.UUID;

public record CarDto(UUID id,
                     String brand,
                     String model,
                     String color,
                     CarType carType,
                     int year,
                     BigDecimal price,
                     boolean isAvailable) {
}
