package com.example.carsharing.sercive;

import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.exceptions.RentalIsNotActiveException;
import com.example.carsharing.mapper.RentalMapper;
import com.example.carsharing.model.Car;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.CarRepository;
import com.example.carsharing.repository.RentalRepository;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.CarService;
import com.example.carsharing.service.impl.RentalServiceImpl;
import com.example.carsharing.supplier.CarSupplier;
import com.example.carsharing.supplier.RentalSupplier;
import com.example.carsharing.supplier.UserSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class RentalServiceTest {
    private static final BigDecimal RENTAL_FINE_PER_DAY = BigDecimal.valueOf(2);
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalMapper rentalMapper;
    @Mock
    private CarService carService;
    @Mock
    private CarRepository carRepository;
    @InjectMocks
    private RentalServiceImpl rentalService;

    @Test
    @DisplayName("Verify save() method works")
    public void save_ValidCreateRequestDto_ReturnsRentalDto() {
        RentalCreationRequestDto requestDto =
                RentalSupplier.getCreateRentalRequestDto();
        RentalWithDetailedCarInfoDto expected =
                RentalSupplier.getRentalWithDetailedCarInfoDtoWithId4();
        Rental rental = RentalSupplier.getRental();
        User user = UserSupplier.getUser();
        user.setRentals(new HashSet<>());
        Car car = CarSupplier.getCar();

        when(carRepository.findById(car.getId())).thenReturn(Optional.of(car));
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(rentalMapper.toModel(requestDto)).thenReturn(rental);
        when(rentalRepository.save(rental)).thenReturn(rental);
        when(rentalMapper.toWithDetailedCarInfoDto(rental)).thenReturn(expected);


        RentalWithDetailedCarInfoDto actual = rentalService.save(requestDto, user.getId());

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(carRepository, times(1)).findById(car.getId());
        verify(rentalMapper, times(1)).toModel(requestDto);
        verify(rentalRepository, times(1)).save(rental);
        verify(rentalMapper, times(1)).toWithDetailedCarInfoDto(rental);
        verifyNoMoreInteractions(rentalRepository, rentalMapper,
                carRepository);
    }

    @Test
    @DisplayName("Verify save() method throws exception when car does not exist")
    public void save_InvalidCreateRequestDto_ThrowsException() {
        RentalCreationRequestDto requestDto = RentalSupplier.getCreateRentalRequestDto();
        User user = UserSupplier.getUser();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(carRepository.findById(requestDto.carId())).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.save(requestDto, user.getId())
        );

        String expected = "There is no car with id: " + requestDto.carId();
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(userRepository, times(1)).findById(user.getId());
        verify(carRepository, times(1)).findById(requestDto.carId());
    }


    @Test
    @DisplayName("Verify the correct rental was returned when rental exists")
    public void findById_ValidId_ReturnsRentalDto() {
        UUID rentalId = UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4");
        Rental rental = RentalSupplier.getRental();
        RentalWithDetailedCarInfoDto expected =
                RentalSupplier.getRentalWithDetailedCarInfoDtoWithId4();

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        when(rentalMapper.toWithDetailedCarInfoDto(rental)).thenReturn(expected);

        RentalWithDetailedCarInfoDto actual = rentalService.findById(rentalId);

        assertNotNull(actual);
        assertEquals(expected, actual);
        verify(rentalRepository, times(1)).findById(rentalId);
        verify(rentalMapper, times(1)).toWithDetailedCarInfoDto(rental);
        verifyNoMoreInteractions(rentalRepository, rentalMapper);
    }

    @Test
    @DisplayName("Verify the exception was thrown when invalid id")
    public void findById_InvalidId_ThrowsException() {
        UUID rentalId = UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4");


        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.findById(rentalId)
        );

        String expected = "There is no rental with id: " + rentalId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(rentalRepository, times(1)).findById(rentalId);
        verifyNoMoreInteractions(rentalRepository, rentalMapper);
    }

    @Test
    @DisplayName("Verify returnRental() method works")
    public void returnRental_ValidId_ReturnsRentalDto() {
        UUID rentalId = UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4");
        Rental rental = RentalSupplier.getRental();

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));
        rentalService.completeRental(rentalId);

        verify(carService, times(1)).returnRentedCar(rental.getCar().getId());
        verify(rentalRepository, times(1)).findById(rentalId);
        verifyNoMoreInteractions(rentalRepository, rentalMapper, carService);
    }

    @Test
    @DisplayName("Verify returnRental() method throws exception when rental does not exists")
    public void returnRental_InvalidId_ThrowsException() {
        UUID rentalId = UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4");

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(
                EntityNotFoundException.class,
                () -> rentalService.completeRental(rentalId)
        );

        String expected = "There is no rental with id: " + rentalId;
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(rentalRepository, times(1)).findById(rentalId);
        verifyNoMoreInteractions(rentalRepository, rentalMapper, carService);
    }

    @Test
    @DisplayName("Verify returnRental() method throws exception when rental is already returned")
    public void returnRental_ReturnedRental_ThrowsException() {
        UUID rentalId = UUID.fromString("abc2b2a9-53dc-4bc8-824f-64448bb463d4");

        Rental rental = RentalSupplier.getRental();
        rental.setActualRentalEnd(LocalDateTime.now());
        rental.setReturned(true);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        RentalIsNotActiveException exception = assertThrows(
                RentalIsNotActiveException.class,
                () -> rentalService.completeRental(rentalId)
        );

        String expected = "Rental is already returned";
        String actual = exception.getMessage();

        assertEquals(expected, actual);
        verify(rentalRepository, times(1)).findById(rentalId);
        verifyNoMoreInteractions(rentalRepository, rentalMapper, carService);
    }

    @Test
    @DisplayName("Should calculate amount based on actualRentalEnd when rental is returned")
    void getAmountToPay_ReturnedRental_CalculatesByActualRentalEnd() {
        UUID rentalId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.of(2025, 5, 1, 15, 15);
        LocalDateTime actualEnd = LocalDateTime.of(2025, 5, 5, 15, 15);
        BigDecimal pricePerDay = new BigDecimal("100");

        Car car = new Car();
        car.setPrice(pricePerDay);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setRentalStart(start);
        rental.setRentalEnd(start.plusDays(10));
        rental.setActualRentalEnd(actualEnd);
        rental.setCar(car);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        BigDecimal expected = pricePerDay.multiply(BigDecimal.valueOf(4));
        BigDecimal actual = rentalService.getAmountToPay(rentalId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should calculate amount based on rental period if rental is ongoing and not overdue")
    void getAmountToPay_OngoingRental_CalculatesBaseAmount() {
        UUID rentalId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().minusDays(2);
        LocalDateTime end = LocalDateTime.now().plusDays(3);
        BigDecimal pricePerDay = new BigDecimal("80");

        Car car = new Car();
        car.setPrice(pricePerDay);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setRentalStart(start);
        rental.setRentalEnd(end);
        rental.setActualRentalEnd(null);
        rental.setCar(car);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        long plannedDays = ChronoUnit.DAYS.between(start, end);
        BigDecimal expected = pricePerDay.multiply(BigDecimal.valueOf(plannedDays));
        BigDecimal actual = rentalService.getAmountToPay(rentalId);

        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Should calculate base amount + fine if rental is overdue and not returned")
    void getAmountToPay_OverdueRental_AddsPenalty() {
        UUID rentalId = UUID.randomUUID();
        LocalDateTime start = LocalDateTime.now().minusDays(10);
        LocalDateTime end = LocalDateTime.now().minusDays(5);
        BigDecimal pricePerDay = new BigDecimal("70");

        Car car = new Car();
        car.setPrice(pricePerDay);

        Rental rental = new Rental();
        rental.setId(rentalId);
        rental.setRentalStart(start);
        rental.setRentalEnd(end);
        rental.setActualRentalEnd(null);
        rental.setCar(car);

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(rental));

        long plannedDays = ChronoUnit.DAYS.between(start, end);
        long overdueDays = ChronoUnit.DAYS.between(end, LocalDateTime.now());

        BigDecimal base = pricePerDay.multiply(BigDecimal.valueOf(plannedDays));
        BigDecimal fine = RENTAL_FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));
        BigDecimal expected = base.add(fine);

        BigDecimal actual = rentalService.getAmountToPay(rentalId);

        assertEquals(expected, actual);
    }

}
