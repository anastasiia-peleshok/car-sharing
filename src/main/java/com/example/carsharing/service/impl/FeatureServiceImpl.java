package com.example.carsharing.service.impl;

import com.example.carsharing.dto.feature.FeatureDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.mapper.FeatureMapper;
import com.example.carsharing.model.Feature;
import com.example.carsharing.repository.FeatureRepository;
import com.example.carsharing.service.FeatureService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FeatureServiceImpl implements FeatureService {
    private final FeatureRepository featureRepository;
    private final FeatureMapper featureMapper;

    @Override
    public FeatureDto getFeatureById(UUID id) {
        Feature feature = getFeature(id);
        return featureMapper.toDto(feature);
    }

    @Override
    public List<FeatureDto> getFeatures() {
        return featureRepository.findAll().stream()
                .map(featureMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public FeatureDto saveFeature(FeatureDto featureDto) {
        Feature future = featureMapper.toModel(featureDto);
        return featureMapper.toDto(featureRepository.save(future));
    }

    @Override
    public FeatureDto updateFeature(UUID id, FeatureDto featureDto) {
        Feature feature = getFeature(id);
        featureMapper.updateFeatureFromDto(featureDto, feature);
        return featureMapper.toDto(featureRepository.save(feature));
    }

    @Override
    public void deleteFeatureById(UUID id) {
        Feature feature = getFeature(id);
        featureRepository.delete(feature);
    }

    private Feature getFeature(UUID id) {
        return featureRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("There is no feature with this id " + id));
    }
}
