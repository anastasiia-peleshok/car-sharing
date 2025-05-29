package com.example.carsharing.service.impl;

import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.dto.car.CarUpdateRequestDto;
import com.example.carsharing.dto.filter.FilterDto;
import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.NoAvailableCarsException;
import com.example.carsharing.mapper.CarMapper;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.CarType;
import com.example.carsharing.repository.CarRepository;
import com.example.carsharing.service.CarService;
import com.example.carsharing.service.PaymentService;
import com.example.carsharing.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CarRegistrationRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        LocalDate now = LocalDate.now();
        car.setAvailable(true);
        car.setCreatedAt(now);
        car.setUpdatedAt(now);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public CarDto getCarById(UUID id) {
        return carMapper.toDto(getCarOrThrow(id));
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public List<CarDto> getAvailableCars() {
        return carRepository.findAllByIsAvailable(true).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public List<CarDto> getCarsByFilter(List<FilterDto> filters) {
        filters.forEach(filter -> {
            if ("type".equals(filter.getColumnName())) {
                filter.setColumnValue(CarType.valueOf((String) filter.getColumnValue()));
            }
        });
        return carRepository.findAll(CarSpecification.columnEqual(filters)).stream()
                .map(carMapper::toDto)
                .toList();
    }

    @Override
    public void rentCar(UUID id) {
        Car car = getCarOrThrow(id);
        if (!car.isAvailable()) {
            throw new NoAvailableCarsException("No available car with id: " + id);
        }
        car.setAvailable(false);
        car.setUpdatedAt(LocalDate.now());
        carRepository.save(car);
    }

    @Override
    public void returnRentedCar(UUID id) {
        Car car = getCarOrThrow(id);
        car.setAvailable(true);
        car.setUpdatedAt(LocalDate.now());
        carRepository.save(car);
    }

    @Override
    public CarDto updateCar(UUID id, CarUpdateRequestDto updateRequestDto) {
        Car car = getCarOrThrow(id);
        car.setUpdatedAt(LocalDate.now());
        carMapper.updateCarFromDto(updateRequestDto, car);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public void deleteCar(UUID id) {
        Car car = getCarOrThrow(id);
        car.setUpdatedAt(LocalDate.now());
        car.setDeletedAt(LocalDate.now());
        carRepository.deleteById(id);
    }

    private Car getCarOrThrow(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + id));
    }
}
