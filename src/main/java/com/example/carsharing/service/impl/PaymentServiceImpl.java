package com.example.carsharing.service.impl;

import com.example.carsharing.dto.payment.PaymentCreationRequestDto;
import com.example.carsharing.dto.payment.PaymentResponseDto;
import com.example.carsharing.exceptions.EntityNotFoundException;
import com.example.carsharing.mapper.PaymentMapper;
import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.Status;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.PaymentRepository;
import com.example.carsharing.repository.RentalRepository;
import com.example.carsharing.repository.UserRepository;
import com.example.carsharing.service.PaymentService;
import com.example.carsharing.service.RentalService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {
    private final PaymentMapper paymentMapper;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final RentalRepository rentalRepository;
    private final WayForPayService wayForPayService;


    @Override
    public PaymentResponseDto createPayment(PaymentCreationRequestDto paymentRequestDto) {
        Rental rental = rentalRepository.findById(paymentRequestDto.rentalId())
                .orElseThrow(() -> new EntityNotFoundException("There is no rental with id: " + paymentRequestDto.rentalId()));

        User user = userRepository.findById(paymentRequestDto.userId())
                .orElseThrow(() -> new EntityNotFoundException("There is no user with id: " + paymentRequestDto.userId()));

        Payment payment = paymentMapper.toModel(paymentRequestDto);
        payment.setUser(user);
        payment.setRental(rental);
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
    public List<PaymentResponseDto> getAllPayments() {
        return paymentRepository.findAll().stream()
                .map(paymentMapper::toDto).collect(Collectors.toList());
    }

    @Override
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
    @Scheduled(fixedRate = 3000)
    public void checkPendingPayments() {
        List<Payment> pending = paymentRepository.findByStatus(Status.UNPAID);
        for (Payment payment : pending) {
            boolean paid = false;
            try {
                paid = wayForPayService.checkStatus(payment.getId().toString());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            if (paid) {
                payment.setStatus(Status.PAID);
                payment.setPaymentTime(LocalDateTime.now());
                paymentRepository.save(payment);
            }
        }
    }

    @Override
    public void notifyForOverduePayments() {

    }
}
