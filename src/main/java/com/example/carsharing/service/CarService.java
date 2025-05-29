package com.example.carsharing.service;

import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.dto.car.CarUpdateRequestDto;
import com.example.carsharing.dto.filter.FilterDto;

import java.util.List;
import java.util.UUID;

public interface CarService {
    CarDto save(CarRegistrationRequestDto carRegistrationRequestDto);

    CarDto getCarById(UUID id);

    List<CarDto> getAllCars();

    CarDto updateCar(UUID id, CarUpdateRequestDto carUpdateRequestDto);

    void rentCar(UUID id);

    void returnRentedCar(UUID id);

    void deleteCar(UUID id);

    List<CarDto> getAvailableCars();

    List<CarDto> getCarsByFilter(List<FilterDto> filters);
}
