package com.example.carsharing.dto.car;

import com.example.carsharing.model.CarType;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record FilterCarDto(
        List<String> brands,
        List<String> models,
        List<String> colors,
        List<CarType> carTypes,
        List<Integer> years,
        List<BigDecimal> priceRange,
        LocalDateTime startDate,
        LocalDateTime endDate,
        List<String> features
) {
}
