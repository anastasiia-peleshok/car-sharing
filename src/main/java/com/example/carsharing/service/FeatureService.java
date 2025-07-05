package com.example.carsharing.service;

import com.example.carsharing.dto.feature.FeatureDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface FeatureService {
    FeatureDto getFeatureById(UUID id);

    Page<FeatureDto> getFeatures(Pageable pageable);

    FeatureDto saveFeature(FeatureDto featureDto);

    FeatureDto updateFeature(UUID id, FeatureDto featureDto);

    void deleteFeatureById(UUID id);
}
