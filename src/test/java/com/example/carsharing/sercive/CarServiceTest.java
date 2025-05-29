package com.example.carsharing.sercive;

import com.example.carsharing.dto.car.CarDto;
import com.example.carsharing.dto.car.CarRegistrationRequestDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.NoAvailableCarsException;
import com.example.carsharing.mapper.CarMapper;
import com.example.carsharing.model.Car;
import com.example.carsharing.repository.CarRepository;
import com.example.carsharing.service.impl.CarServiceImpl;
import com.example.carsharing.supplier.CarSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CarServiceTest {
    @InjectMocks
    private CarServiceImpl carService;
    @Mock
    private CarMapper carMapper;
    @Mock
    private CarRepository carRepository;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateRequestDto_ReturnsCarDto() {
        CarRegistrationRequestDto requestDto = CarSupplier.getCreateCarRequestDto();
        CarDto expected = CarSupplier.getCarDto();
        Car car = CarSupplier.getCar();

        when(carMapper.toModel(requestDto)).thenReturn(car);
        when(carRepository.save(car)).thenReturn(car);
        when(carMapper.toDto(car)).thenReturn(expected);

        CarDto actual = carService.save(requestDto);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(carRepository, times(1)).save(car);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

    @Test
    @DisplayName("Verify the correct car was returned when car exists")
    public void findById_ValidId_ReturnsCarDto() {
        UUID carId = UUID.randomUUID();
        Car car = CarSupplier.getCar();
        CarDto expected = CarSupplier.getCarDto();

        when(carRepository.findById(carId)).thenReturn(Optional.of(car));
        when(carMapper.toDto(car)).thenReturn(expected);

        CarDto actual = carService.getCarById(carId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(carRepository, times(1)).findById(carId);
        verify(carMapper, times(1)).toDto(car);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

    @Test
    @DisplayName("Verify the exception was thrown when invalid id")
    public void findById_InvalidId_ThrowsException() {
        UUID carId = UUID.randomUUID();

        when(carRepository.findById(carId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> carService.getCarById(carId)
        );

        String expected = "There is no car with id: " + carId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(carRepository, times(1)).findById(carId);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

    @Test
    @DisplayName("Verify the exception was thrown when car inventory is 0")
    public void rentalCar_ZeroInventory_ThrowsException() {
        Car car = CarSupplier.getCar();
        car.setAvailable(false);

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        NoAvailableCarsException exception = assertThrows(
                NoAvailableCarsException.class,
                () -> carService.rentCar(car.getId())
        );

        String expected = "No available car with id: "+ car.getId();
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verifyNoMoreInteractions(carRepository, carMapper);
    }

}
