package com.example.carsharing.dto.car;

import com.example.carsharing.model.CarType;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

public record CarRegistrationRequestDto(
        @NotNull
        @Length(min = 2, max = 255)
        String brand,
        @NotNull
        @Length(min = 2, max = 255)
        String model,
        @NotNull
        @Length(min = 2, max = 255)
        String color,
        @NotNull
        CarType carType,
        @NotNull
        @Length(min = 2, max = 255)
        int year,
        @NotNull
        @Length(min = 2, max = 255)
        BigDecimal price) {
}
