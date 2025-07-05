package com.example.carsharing.service.impl;

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
import com.example.carsharing.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RentalServiceImpl implements RentalService {
    private final RentalRepository rentalRepository;
    private final UserRepository userRepository;
    private final CarRepository carRepository;
    private final CarService carService;
    private final RentalMapper rentalMapper;

    private static final BigDecimal RENTAL_FINE_PER_DAY = BigDecimal.valueOf(2);

    @Override
    public RentalWithDetailedCarInfoDto save(RentalCreationRequestDto requestDto, UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id: " + userId));

        UUID carId = requestDto.carId();
        Car car = carRepository.findById(carId)
                .orElseThrow(() -> new EntityNotFoundException("There is no car with id: " + carId));

        carService.rentCar(carId);

        LocalDateTime now = LocalDateTime.now();
        Rental rental = rentalMapper.toModel(requestDto);
        rental.setUser(user);
        rental.setCar(car);
        rental.setRentalStart(now);

        Rental savedRental = rentalRepository.save(rental);
        user.getRentals().add(savedRental);
        return rentalMapper.toWithDetailedCarInfoDto(savedRental);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalDto> findAllByUser(UUID userId, Pageable pageable) {
        return rentalRepository.findAllByUserId(userId, pageable)
                .map(rentalMapper::toDto);
    }

    @Override
    public void completeRental(UUID rentalId) {
        Rental rental = getRental(rentalId);

        if (rental.isReturned()) {
            throw new RentalIsNotActiveException("Rental is already returned");
        }
        LocalDateTime now = LocalDateTime.now();
        rental.setReturned(true);
        rental.setActualRentalEnd(now);

        carService.returnRentedCar(rental.getCar().getId());
    }

    @Override
    @Transactional(readOnly = true)
    public RentalWithDetailedCarInfoDto findById(UUID id) {
        return rentalMapper.toWithDetailedCarInfoDto(getRental(id));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<RentalWithDetailedCarInfoDto> checkOverdueRentals(Pageable  pageable) {
        LocalDate today = LocalDate.now();
        return rentalRepository.findAllOverdues(today, pageable)
                .map(rentalMapper::toWithDetailedCarInfoDto);
    }

    @Override
    @Transactional(readOnly = true)
    public BigDecimal getAmountToPay(UUID rentalId) {
        Rental rental = getRental(rentalId);

        LocalDateTime rentalStart = rental.getRentalStart();
        LocalDateTime rentalEnd = rental.getRentalEnd();
        LocalDateTime now = LocalDateTime.now();
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

    private Rental getRental(UUID rentalId) {
        return rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + rentalId));
    }
}
