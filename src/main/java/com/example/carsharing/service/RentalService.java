package com.example.carsharing.service;

import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RentalService {
    RentalWithDetailedCarInfoDto save(RentalCreationRequestDto requestDto, UUID userId);

    void completeRental(UUID id);

    List<RentalDto> findAllByUser(UUID userId);

    RentalWithDetailedCarInfoDto findById(UUID id);

    List<RentalWithDetailedCarInfoDto> checkOverdueRentals();

    BigDecimal getAmountToPay(UUID rentalId);
}
