package com.example.carsharing.service;

import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.UUID;

public interface RentalService {
    RentalWithDetailedCarInfoDto save(RentalCreationRequestDto requestDto, UUID userId);

    void completeRental(UUID id);

    Page<RentalDto> findAllByUser(UUID userId, Pageable pageable);

    RentalWithDetailedCarInfoDto findById(UUID id);

    Page<RentalWithDetailedCarInfoDto> checkOverdueRentals(Pageable pageable);

    BigDecimal getAmountToPay(UUID rentalId);
}
