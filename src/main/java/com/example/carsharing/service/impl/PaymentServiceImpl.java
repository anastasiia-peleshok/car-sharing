package com.example.carsharing.service.impl;

import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.mapper.PaymentMapper;
import com.example.carsharing.model.*;
import com.example.carsharing.repository.PaymentRepository;
import com.example.carsharing.repository.RentalRepository;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.NotificationService;
import com.example.carsharing.service.PaymentService;
import com.example.carsharing.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final RentalService rentalService;
    private final RentalRepository rentalRepository;
    private final WayForPayService wayForPayService;
    private final NotificationService notificationService;


    @Override
    public PaymentResponseDto createPayment(UUID rentalId) {
        Rental rental = rentalRepository.findById(rentalId)
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + rentalId));
        User user = rental.getUser();
        Payment payment = new Payment();
        payment.setUser(user);
        payment.setRental(rental);
        payment.setAmount(rentalService.getAmountToPay(rentalId));
        payment.setStatus(Status.UNPAID);
        Payment savedPayment = paymentRepository.save(payment);

        String paymentLink;
        try {
            paymentLink = wayForPayService.createLink(savedPayment);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create payment link", e);
        }

        return new PaymentResponseDto(
                savedPayment.getAmount(),
                savedPayment.getStatus(),
                savedPayment.getUser().getId(),
                savedPayment.getRental().getId(),
                savedPayment.getPaymentTime(),
                paymentLink
        );
    }


    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable)
                .map(paymentMapper::toDto);
    }

    @Override
    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(UUID id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("There is no payments with id: " + id));
        String paymentLink;
        try {
            paymentLink = wayForPayService.createLink(payment);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new PaymentResponseDto(
                payment.getAmount(),
                payment.getStatus(),
                payment.getUser().getId(),
                payment.getRental().getId(),
                payment.getPaymentTime(),
                paymentLink);
    }

    @Override
    @Transactional(readOnly = true)
    //@Scheduled(fixedRate = 3000)
    public void checkPendingPayments() {
        List<Payment> pending = paymentRepository.findByStatus(Status.UNPAID);
        for (Payment payment : pending) {
            boolean paid;
            try {
                paid = wayForPayService.checkStatus(payment.getId().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (paid) {
                payment.setStatus(Status.PAID);
                payment.setPaymentTime(LocalDateTime.now());
                User user = payment.getUser();
                notificationService.sendNotification(
                        user,
                        NotificationSubject.BOOKING_CONFIRMED.toString(),
                        "Your payment for car rental is proceeded."
                );
            }
        }
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PaymentResponseDto> getPaymentByStatus(Status status, Pageable pageable) {
        return paymentRepository.findAllByStatus(status, pageable).map(paymentMapper::toDto);
    }

    @Override
    public void notifyForOverduePayments() {

    }
}
