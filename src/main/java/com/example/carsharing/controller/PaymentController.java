package com.example.carsharing.controller;

import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.model.Status;
import com.example.carsharing.service.PaymentService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    /**
     * Create a new payment for a rental.
     */
    @Operation(summary = "Create payment", description = "Create a new payment for a specific rental and user.")
    @PostMapping("/{rentalId}")
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponseDto createPayment(@PathVariable UUID rentalId) {
        return paymentService.createPayment(rentalId);
    }

    /**
     * Get all payments.
     */
    @Operation(summary = "Get all payments", description = "Retrieve a list of all payments.")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponseDto> getAllPayments(Pageable pageable) {
        return paymentService.getAllPayments(pageable);
    }

    /**
     * Get payment details by ID.
     */
    @Operation(summary = "Get payment by ID", description = "Retrieve details of a specific payment by its ID.")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PaymentResponseDto getPaymentById(@PathVariable UUID id) {
        return paymentService.getPaymentById(id);
    }

    /**
     * Get payment details by ID.
     */
    @Operation(summary = "Get payment by ID", description = "Retrieve details of a specific payment by its ID.")
    @GetMapping("/status/{status}")
    @ResponseStatus(HttpStatus.OK)
    public Page<PaymentResponseDto> getPaymentByStatus(@PathVariable Status status, Pageable pageable) {
        return paymentService.getPaymentByStatus(status, pageable);
    }

}
