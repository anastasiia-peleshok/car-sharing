package com.example.carsharing.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.feature.FeatureDto;
import com.example.carsharing.model.Feature;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface FeatureMapper {

    Feature toModel(FeatureDto requestDto);

    FeatureDto toDto(Feature feature);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateFeatureFromDto(FeatureDto requestDto, @MappingTarget Feature feature);
}
