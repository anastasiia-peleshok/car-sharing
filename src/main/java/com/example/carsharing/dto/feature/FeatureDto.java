package com.example.carsharing.dto.feature;

import jakarta.validation.constraints.NotNull;

public record FeatureDto(
        @NotNull
        String name,
        @NotNull
        String description
) {
}
