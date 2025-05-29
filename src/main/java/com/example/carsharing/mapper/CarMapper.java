package com.example.carsharing.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.dto.car.CarUpdateRequestDto;
import com.example.carsharing.model.Car;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

@Mapper(config = MapperConfig.class)
public interface CarMapper {
    CarDto toDto(Car car);

    Car toModel(CarRegistrationRequestDto requestDto);
    Car toModel(CarUpdateRequestDto requestDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCarFromDto(CarUpdateRequestDto requestDto, @MappingTarget Car car);
}
