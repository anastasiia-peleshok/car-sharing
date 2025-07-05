package com.example.carsharing.repository;

import com.example.carsharing.dto.car.FilterCarDto;
import com.example.carsharing.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCarRepository {
    Page<Car> findByFilterAndDateRange(FilterCarDto filterCarDto, Pageable pageable);
}
