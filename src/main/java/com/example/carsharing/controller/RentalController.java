package com.example.carsharing.controller;

import com.example.carsharing.dto.rental.RentalCreationRequestDto;
import com.example.carsharing.dto.rental.RentalDto;
import com.example.carsharing.dto.rental.RentalWithDetailedCarInfoDto;
import com.example.carsharing.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.UUID;

@RestController
@RequestMapping("/rental")
@RequiredArgsConstructor
@Validated
public class RentalController {
    private final RentalService rentalService;

    /**
     * Create a new rental request for a specific user.
     */
    @Operation(summary = "Create rental request", description = "Create a new rental request for a specific user")
    @PostMapping("/{userId}")
    @ResponseStatus(HttpStatus.CREATED)
    public RentalWithDetailedCarInfoDto createRentalRequest(
            @RequestBody RentalCreationRequestDto rentalCreationRequestDto,
            @PathVariable UUID userId) {
        return rentalService.save(rentalCreationRequestDto, userId);
    }

    /**
     * Mark the rental as returned by updating the actual return date.
     */
    @Operation(summary = "Return rented car", description = "Mark the rental as returned by updating the actual return date")
    @PostMapping("/return/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public void returnRental(@PathVariable UUID rentalId) {
        rentalService.completeRental(rentalId);
    }

    /**
     * Get the cost of a rental (including penalty if overdue).
     */
    @Operation(summary = "Get cost of rental", description = "Get the cost of a rental (including penalty if overdue)")
    @PostMapping("/cost/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public BigDecimal getRentalCost(@PathVariable UUID rentalId) {
        return rentalService.getAmountToPay(rentalId);
    }

    /**
     * Get rental details by ID. Accessible to MANAGER only.
     */
    @Operation(summary = "Get rental details", description = "Get rental details by ID. Accessible to MANAGER only")
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/{rentalId}")
    @ResponseStatus(HttpStatus.OK)
    public RentalWithDetailedCarInfoDto getRental(@PathVariable UUID rentalId) {
        return rentalService.findById(rentalId);
    }

    /**
     * Get all rentals for a specific user.
     */
    @Operation(summary = "Get all rentals by user", description = "Retrieve all rentals for a specific user.")
    @GetMapping("/user/{userId}")
    @ResponseStatus(HttpStatus.OK)
    public Page<RentalDto> getAllRentalsByUser(@PathVariable UUID userId, Pageable pageable) {
        return rentalService.findAllByUser(userId, pageable);
    }

    /**
     * Get all overdue rentals. Accessible to MANAGER only.
     */
    @Operation(summary = "Get all overdue rentals", description = "Get all overdue rentals. Accessible to MANAGER only.")
    @PreAuthorize("hasAuthority('MANAGER')")
    @GetMapping("/overdue")
    @ResponseStatus(HttpStatus.OK)
    public Page<RentalWithDetailedCarInfoDto> getOverdueRentals(Pageable  pageable) {
        return rentalService.checkOverdueRentals(pageable);
    }
}
