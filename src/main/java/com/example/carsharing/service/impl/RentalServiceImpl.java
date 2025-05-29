package com.example.carsharing.service.impl;

import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
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
import com.example.carsharing.service.PaymentService;
import com.example.carsharing.service.RentalService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final RentalMapper rentalMapper;
    private final PaymentService paymentService;

    private static final BigDecimal RENTAL_FINE_PER_DAY = BigDecimal.valueOf(2);

    @Override
    @Transactional
    public RentalWithDetailedCarInfoDto save(RentalCreationRequestDto requestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id: " + userId));

        UUID carId = requestDto.carId();
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + carId));

        carService.rentCar(carId);

        LocalDate now = LocalDate.now();
        Rental rental = rentalMapper.toModel(requestDto);
        rental.setUser(user);
        rental.setCar(car);
        rental.setCreatedAt(now);
        rental.setUpdatedAt(now);
        rental.setRentalStart(now);

        Rental savedRental = rentalRepository.save(rental);
        user.getRentalList().add(savedRental);
        return rentalMapper.toWithDetailedCarInfoDto(savedRental);
    }

    @Override
    public List<RentalDto> findAllByUser(UUID userId) {
        return rentalRepository.findAllByUserId(userId).stream()
                .map(rentalMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public PaymentResponseDto completeRental(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + rentalId));

        if(rental.isReturned()){
            throw new RentalIsNotActiveException("Rental is already returned");
        }
        LocalDate now = LocalDate.now();
        rental.setReturned(true);
        rental.setActualRentalEnd(now);
        carService.returnRentedCar(rental.getCar().getId());

        PaymentCreationRequestDto paymentCreationRequestDto = new PaymentCreationRequestDto(rental.getUser().getId(), rental.getCar().getId());
        return paymentService.createPayment(paymentCreationRequestDto);
    }

    @Override
    public RentalWithDetailedCarInfoDto findById(UUID id) {
        return rentalRepository.findById(id)
                .map(rentalMapper::toWithDetailedCarInfoDto)
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + id));
    }

    @Override
    public List<RentalWithDetailedCarInfoDto> checkOverdueRentals() {
        LocalDate today = LocalDate.now();
        return rentalRepository.findAllOverdues(today).stream()
                .map(rentalMapper::toWithDetailedCarInfoDto)
                .toList();
    }

    @Override
    public BigDecimal getAmountToPay(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + rentalId));

        LocalDate rentalStart = rental.getRentalStart();
        LocalDate rentalEnd = rental.getRentalEnd();
        LocalDate now = LocalDate.now();
        BigDecimal dailyRate = rental.getCar().getPrice();

        if (rental.getActualRentalEnd() != null) {
            long usedDays = ChronoUnit.DAYS.between(rentalStart, rental.getActualRentalEnd());
            return dailyRate.multiply(BigDecimal.valueOf(usedDays));
        }

        long plannedDays = ChronoUnit.DAYS.between(rentalStart, rentalEnd);
        BigDecimal baseAmount = dailyRate.multiply(BigDecimal.valueOf(plannedDays));

        if (!rentalEnd.isBefore(now)) {
            return baseAmount;
        }

        long overdueDays = ChronoUnit.DAYS.between(rentalEnd, now);
        BigDecimal penalty = RENTAL_FINE_PER_DAY.multiply(BigDecimal.valueOf(overdueDays));

        return baseAmount.add(penalty);
    }
}
