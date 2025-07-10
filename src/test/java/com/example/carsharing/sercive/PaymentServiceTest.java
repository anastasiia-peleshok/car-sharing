package com.example.carsharing.sercive;

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
import com.example.carsharing.service.NotificationService;
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.impl.PaymentServiceImpl;
import com.example.carsharing.service.impl.WayForPayService;
import com.example.carsharing.supplier.PaymentSupplier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private NotificationService notificationService;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private WayForPayService wayForPayService;
    @Mock
    private RentalService rentalService;

    @Test
    @DisplayName("Verify createPayment returns PaymentResponseDto when all is valid")
    void testCreatePayment_Success() throws Exception {
        Rental testRental = PaymentSupplier.rental();
        UUID rentalId = testRental.getId();
        BigDecimal amountToPay = BigDecimal.valueOf(100);
        Payment testPayment = PaymentSupplier.unpaidPayment();

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(testRental));
        when(rentalService.getAmountToPay(rentalId)).thenReturn(amountToPay);
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(wayForPayService.createLink(any(Payment.class))).thenReturn("http://payment.link");

        PaymentResponseDto result = paymentService.createPayment(rentalId);

        assertNotNull(result);
        assertEquals(testPayment.getAmount(), result.amount());
        assertEquals(testPayment.getStatus(), result.status());
        assertEquals(testPayment.getUser().getId(), result.userId());
        assertEquals(testPayment.getRental().getId(), result.rentalId());
        assertEquals("http://payment.link", result.paymentLink());

        verify(paymentRepository, times(1)).save(any(Payment.class));
        verify(wayForPayService, times(1)).createLink(any(Payment.class));
    }

    @Test
    @DisplayName("Verify exception was thrown when rental not found")
    void testCreatePayment_RentalNotFound() {
        UUID fakeRentalId = UUID.randomUUID();
        when(rentalRepository.findById(fakeRentalId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.createPayment(fakeRentalId));
        verify(paymentRepository, never()).save(any());
    }

    @Test
    @DisplayName("Verify exception was thrown when payment link creation fails")
    void testCreatePayment_PaymentLinkCreationFails() throws Exception {
        Rental testRental = PaymentSupplier.rental();
        UUID rentalId = testRental.getId();
        BigDecimal amountToPay = BigDecimal.valueOf(100);
        Payment testPayment = PaymentSupplier.unpaidPayment();

        when(rentalRepository.findById(rentalId)).thenReturn(Optional.of(testRental));
        when(rentalService.getAmountToPay(rentalId)).thenReturn(amountToPay);
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(wayForPayService.createLink(any())).thenThrow(new RuntimeException("WayForPay error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.createPayment(rentalId));
        assertEquals("Failed to create payment link", ex.getMessage());
    }

    @Test
    @DisplayName("Verify exception was thrown when WayForPay status check fails in checkPendingPayments")
    void testCheckPendingPayments_WayForPayStatusCheckFails() {
        Payment testPayment = PaymentSupplier.unpaidPayment();
        when(paymentRepository.findByStatus(Status.UNPAID)).thenReturn(List.of(testPayment));
        try {
            when(wayForPayService.checkStatus(anyString())).thenThrow(new RuntimeException("WayForPay check failed"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.checkPendingPayments());
        assertEquals("WayForPay check failed", ex.getCause().getMessage());
    }

    @Test
    @DisplayName("Verify checkPendingPayments updates status to PAID when payment is successful")
    void testCheckPendingPayments_UpdatesStatusToPaid() throws Exception {
        Payment testPayment = PaymentSupplier.unpaidPayment();
        when(paymentRepository.findByStatus(Status.UNPAID)).thenReturn(List.of(testPayment));
        when(wayForPayService.checkStatus(anyString())).thenReturn(true);

        assertDoesNotThrow(() -> paymentService.checkPendingPayments());
    }
}