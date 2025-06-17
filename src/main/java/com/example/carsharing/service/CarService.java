package com.example.carsharing.service;

import com.example.carsharing.dto.car.*;
import com.example.carsharing.dto.filter.FilterDto;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto save(CarRegistrationRequestDto carRegistrationRequestDto);

    CarDto getCarById(UUID id);

    List<CarDto> getAllCars();

    CarDto updateCar(UUID id, CarUpdateRequestDto carUpdateRequestDto);

    FullCarDto getCarByIdWithRelations(UUID id);

    void rentCar(UUID id);

    void returnRentedCar(UUID id);

    void deleteCar(UUID id);

    List<CarDto> getAvailableCars();

    List<CarDto> getCarsByRangeAndFilter(FilterCarDto filterCarDto);

    List<CarDto> getCarsByFilter(List<FilterDto> filters);

    CarDto setNewFeatureToCar(UUID carId, UUID featureId);

    CarDtoWithFeatures getByIdWithFeatures(UUID carId);
}
