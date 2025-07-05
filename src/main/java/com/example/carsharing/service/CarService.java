package com.example.carsharing.service;

import com.example.carsharing.dto.car.*;
import com.example.carsharing.dto.filter.FilterDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto save(CarRegistrationRequestDto carRegistrationRequestDto);

    CarDto getCarById(UUID id);

    Page<CarDto> getAllCars(Pageable pageable);

    CarDto updateCar(UUID id, CarUpdateRequestDto carUpdateRequestDto);

    FullCarDto getCarByIdWithRelations(UUID id);

    void rentCar(UUID id);

    void returnRentedCar(UUID id);

    void deleteCar(UUID id);

    Page<CarDto> getAvailableCars(Pageable pageable);

    Page<CarDto> getCarsByRangeAndFilter(FilterCarDto filterCarDto, Pageable pageable);

    Page<CarDto> getCarsByFilter(List<FilterDto> filters, Pageable pageable);

    CarDto setNewFeatureToCar(UUID carId, UUID featureId);

    CarDtoWithFeatures getByIdWithFeatures(UUID carId);
}
