package com.example.carsharing.repository;

import com.example.carsharing.dto.car.FilterCarDto;
import com.example.carsharing.model.Car;

import java.util.List;

public interface CustomCarRepository {
    List<Car> findByFilterAndDateRange(FilterCarDto filterCarDto);
}
