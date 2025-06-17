package com.example.carsharing.controller;

import com.example.carsharing.dto.car.*;
import com.example.carsharing.dto.filter.FilterDto;
import com.example.carsharing.service.CarService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {
    private final CarService carService;

    /**
     * Get all cars from the system.
     */
    @Operation(summary = "Get all cars", description = "Retrieve all cars from database")
    @GetMapping
    public List<CarDto> getAllCars() {
        return carService.getAllCars();
    }

    /**
     * Get all cars matching a list of filter criteria.
     */
    @Operation(summary = "Get cars by filter", description = "Retrieve all cars from database by filter criteria")
    @GetMapping("/filter")
    public List<CarDto> getAllCarsByFilter(@RequestBody List<FilterDto> filters) {
        return carService.getCarsByFilter(filters);
    }

    /**
     * Add feature into car.
     */
    @Operation(summary = "Add feature to car", description = "Add")
    @PostMapping("/{carId}/{featureId}")
    @PreAuthorize("hasAuthority('MANAGER')")
    public CarDto addFeatureToCar(@PathVariable UUID carId, @PathVariable UUID featureId ) {
        return carService.setNewFeatureToCar(carId, featureId);
    }

    /**
     * Get a specific car by its ID.
     */
    @Operation(summary = "Get car by ID", description = "Retrieve a specific car by its UUID")
    @GetMapping("/{carId}")
    public CarDto getCarById(@PathVariable UUID carId) {
        return carService.getCarById(carId);
    }

    /**
     * Get a specific car by its ID with features.
     */
    @Operation(summary = "Get car by ID with features", description = "Retrieve a specific car by its UUID")
    @GetMapping("/{carId}/features")
    public CarDtoWithFeatures getCarWithFeatures(@PathVariable UUID carId) {
        return carService.getByIdWithFeatures(carId);
    }

    /**
     * Get a specific car by its ID with details.
     */
    @Operation(summary = "Get car by ID with details", description = "Retrieve a specific car by its UUID")
    @GetMapping("/{carId}/details")
    public FullCarDto getCarWithAllRelations(@PathVariable UUID carId) {
        return carService.getCarByIdWithRelations(carId);
    }

    /**
     * Get all cars that is currently available.
     */
    @Operation(summary = "Get all currently available cars", description = "Retrieve available cars")
    @GetMapping("/available")
    public List<CarDto> getAllAvailableCars() {
        return carService.getAvailableCars();
    }

    /**
     * Get all cars that are available for date range with filters.
     */
    @Operation(summary = "Get all available cars by filter", description = "Retrieve available cars")
    @PostMapping("/available")
    public List<CarDto> getAllAvailableCarsByRangeAndFilter(@RequestBody FilterCarDto filterCarDto) {
        return carService.getCarsByRangeAndFilter(filterCarDto);
    }

    /**
     * Register a new car in the system.
     */
    @Operation(summary = "Add car", description = "Add a new car to the system")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasAuthority('MANAGER')")
    public CarDto addCar(@RequestBody CarRegistrationRequestDto requestDto) {
        return carService.save(requestDto);
    }

    /**
     * Update an existing car's properties.
     */
    @Operation(summary = "Update car", description = "Update an existing car's details")
    @PutMapping("/{carId}")
    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAuthority('MANAGER')")
    public CarDto updateCar(@PathVariable UUID carId, @RequestBody CarUpdateRequestDto requestDto) {
        return carService.updateCar(carId, requestDto);
    }

    /**
     * Soft delete (deactivate) a car from the system.
     */
    @Operation(summary = "Delete car", description = "Deactivate (soft delete) a car")
    @DeleteMapping("/{carId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasAuthority('MANAGER')")
    public void deleteCar(@PathVariable UUID carId) {
        carService.deleteCar(carId);
    }
}
