package com.example.carsharing.service;

import com.example.carsharing.dto.feature.FeatureDto;

import java.util.List;
import java.util.UUID;

public interface FeatureService {
    FeatureDto getFeatureById(UUID id);

    List<FeatureDto> getFeatures();

    FeatureDto saveFeature(FeatureDto featureDto);

    FeatureDto updateFeature(UUID id, FeatureDto featureDto);

    void deleteFeatureById(UUID id);
}
