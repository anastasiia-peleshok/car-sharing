package com.example.carsharing.dto.car;

import com.example.carsharing.model.CarType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record CarUpdateRequestDto(
        @Length(min = 2, max = 255)
        String brand,
        @Length(min = 2, max = 255)
        String model,
        @Length(min = 2, max = 255)
        String color,
        CarType carType,
        @Length(min = 2, max = 255)
        int year,
        @Length(min = 2, max = 255)
        BigDecimal price,
        boolean isAvailable) {
}
