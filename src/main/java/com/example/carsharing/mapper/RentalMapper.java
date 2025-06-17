package com.example.carsharing.mapper;

import com.example.carsharing.config.MapperConfig;
import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;
import com.example.carsharing.model.Rental;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(config = MapperConfig.class)
public interface RentalMapper {
    Rental toModel(RentalCreationRequestDto requestDto);

    @Mapping(target = "userId", expression = "java(rental.getUser().getId())")
    @Mapping(target = "carId", expression = "java(rental.getCar().getId())")
    RentalDto toDto(Rental rental);

    @Mapping(target = "userId", expression = "java(rental.getUser().getId())")
    RentalWithDetailedCarInfoDto toWithDetailedCarInfoDto(Rental rental);

}
