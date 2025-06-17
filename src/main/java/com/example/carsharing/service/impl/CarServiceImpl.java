package com.example.carsharing.service.impl;

import com.example.carsharing.dto.car.*;
import com.example.carsharing.dto.filter.FilterDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.NoAvailableCarsException;
import com.example.carsharing.mapper.CarMapper;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.CarType;
import com.example.carsharing.model.Feature;
import com.example.carsharing.repository.CarRepository;
import com.example.carsharing.repository.FeatureRepository;
import com.example.carsharing.service.CarService;
import com.example.carsharing.specification.CarSpecification;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarServiceImpl implements CarService {
    private final FeatureRepository featureRepository;
    private final CarRepository carRepository;
    private final CarMapper carMapper;

    @Override
    public CarDto save(CarRegistrationRequestDto requestDto) {
        Car car = carMapper.toModel(requestDto);
        car.setAvailable(true);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public CarDto getCarById(UUID id) {
        return carMapper.toDto(carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + id)));
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
        carRepository.save(car);
    }

    @Override
    public void returnRentedCar(UUID id) {
        Car car = getCarOrThrow(id);
        car.setAvailable(true);
        carRepository.save(car);
    }

    @Override
    public CarDto updateCar(UUID id, CarUpdateRequestDto updateRequestDto) {
        Car car = getCarOrThrow(id);
        carMapper.updateCarFromDto(updateRequestDto, car);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    public void deleteCar(UUID id) {
        Car car = getCarOrThrow(id);
        car.setDeletedAt(LocalDateTime.now());
        carRepository.deleteById(id);
    }


    @Override
    public CarDto setNewFeatureToCar(UUID carId, UUID featureId) {
        Feature feature = featureRepository.findById(featureId).orElseThrow(() -> new EntityNotFoundException("There is no feature with id: " + featureId));
        Car car = getCarOrThrow(carId);

        car.getFeatures().add(feature);
        return carMapper.toDto(carRepository.save(car));
    }

    @Override
    @Transactional(readOnly = true)
    public CarDtoWithFeatures getByIdWithFeatures(UUID carId) {
        Car car = carRepository.findByIdWithFeatures(carId)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + carId));
        return carMapper.toFeaturesDto(car);
    }


    @Override
    @Transactional(readOnly = true)
    public FullCarDto getCarByIdWithRelations(UUID id) {
        Car car = carRepository.findByIdWithAllRelations(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + id));

        return carMapper.toFullCarDto(car);
    }

    @Override
    public List<CarDto> getCarsByRangeAndFilter(FilterCarDto filterCarDto) {
        return carRepository.findByFilterAndDateRange(filterCarDto).stream()
                .map(carMapper::toDto)
                .collect(Collectors.toList());
    }

    private Car getCarOrThrow(UUID id) {
        return carRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + id));
    }
}
