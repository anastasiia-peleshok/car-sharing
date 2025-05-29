package com.example.carsharing.controller;

import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.dto.car.CarUpdateRequestDto;
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
     * Get a specific car by its ID.
     */
    @Operation(summary = "Get car by ID", description = "Retrieve a specific car by its UUID")
    @GetMapping("/{carId}")
    public CarDto getCarById(@PathVariable UUID carId) {
        return carService.getCarById(carId);
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
