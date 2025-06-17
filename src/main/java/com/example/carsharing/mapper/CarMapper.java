package com.example.carsharing.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.car.*;
import com.example.carsharing.model.Car;
import org.mapstruct.*;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarDto toDto(Car car);

    Car toModel(CarRegistrationRequestDto requestDto);

    Car toModel(CarUpdateRequestDto requestDto);

    @Mapping(target = "features", source = "features")
    @Mapping(target = "rentals", source = "rentals")
    FullCarDto toFullCarDto(Car car);

    @Mapping(target = "features", source = "features")
    CarDtoWithFeatures toFeaturesDto(Car car);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarFromDto(CarUpdateRequestDto requestDto, @MappingTarget Car car);
}
