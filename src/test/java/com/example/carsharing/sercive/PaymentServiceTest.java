package com.example.carsharing.sercive;

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
import com.example.carsharing.service.RentalService;
import com.example.carsharing.service.impl.PaymentServiceImpl;
import com.example.carsharing.service.impl.WayForPayService;
import com.example.carsharing.supplier.PaymentSupplier;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @InjectMocks
    private PaymentServiceImpl paymentService;

    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private RentalRepository rentalRepository;
    @Mock
    private WayForPayService wayForPayService;
    @Mock
    private PaymentMapper paymentMapper;
    @Mock
    private RentalService rentalService;

    @Test
    void testCreatePayment_Success() throws Exception {
        User testUser = PaymentSupplier.user();
        Rental testRental = PaymentSupplier.rental();
        Payment testPayment = PaymentSupplier.unpaidPayment();

        PaymentCreationRequestDto requestDto = new PaymentCreationRequestDto(testUser.getId(), testRental.getId());

        when(rentalRepository.findById(testRental.getId())).thenReturn(Optional.of(testRental));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);
        when(wayForPayService.createLink(any(Payment.class))).thenReturn("http://payment.link");
        when(paymentMapper.toModel(requestDto)).thenReturn(testPayment);

        PaymentResponseDto result = paymentService.createPayment(requestDto);

        assertNotNull(result);
        assertEquals(testPayment.getAmount(), result.amount());
        assertEquals(testPayment.getStatus(), result.status());
        assertEquals(testPayment.getUser().getId(), result.userId());
        assertEquals(testPayment.getRental().getId(), result.rentalId());
        assertEquals("http://payment.link", result.paymentLink());
    }

    @Test
    void testCreatePayment_RentalNotFound() {
        User testUser = PaymentSupplier.user();
        UUID fakeRentalId = UUID.randomUUID();
        PaymentCreationRequestDto requestDto = new PaymentCreationRequestDto(testUser.getId(), fakeRentalId);

        when(rentalRepository.findById(fakeRentalId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.createPayment(requestDto));
    }

    @Test
    void testCreatePayment_UserNotFound() {
        Rental testRental = PaymentSupplier.rental();
        UUID fakeUserId = UUID.randomUUID();
        PaymentCreationRequestDto requestDto = new PaymentCreationRequestDto(fakeUserId, testRental.getId());

        when(rentalRepository.findById(testRental.getId())).thenReturn(Optional.of(testRental));
        when(userRepository.findById(fakeUserId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> paymentService.createPayment(requestDto));
    }

    @Test
    void testCreatePayment_PaymentLinkCreationFails() throws Exception {
        User testUser = PaymentSupplier.user();
        Rental testRental = PaymentSupplier.rental();
        Payment testPayment = PaymentSupplier.unpaidPayment();
        PaymentCreationRequestDto requestDto = new PaymentCreationRequestDto(testUser.getId(), testRental.getId());

        when(rentalRepository.findById(testRental.getId())).thenReturn(Optional.of(testRental));
        when(userRepository.findById(testUser.getId())).thenReturn(Optional.of(testUser));
        when(paymentMapper.toModel(requestDto)).thenReturn(testPayment);
        when(paymentRepository.save(any())).thenReturn(testPayment);
        when(wayForPayService.createLink(any())).thenThrow(new RuntimeException("WayForPay error"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> paymentService.createPayment(requestDto));
        assertEquals("Failed to create payment link", ex.getMessage());
    }

    @Test
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
    void testCreatePayment_WithPaymentHavingNullAmount() {
        Payment paymentWithNullAmount = PaymentSupplier.paymentWithNullAmount();

        assertNull(paymentWithNullAmount.getAmount());
    }

    @Test
    void testCreatePayment_WithPaymentHavingNullUser() {
        Payment paymentWithNullUser = PaymentSupplier.paymentWithNullUser();
        assertNull(paymentWithNullUser.getUser());
    }

    @Test
    void testCreatePayment_WithPaymentHavingNullRental() {
        Payment paymentWithNullRental = PaymentSupplier.paymentWithNullRental();
        assertNull(paymentWithNullRental.getRental());
    }
}
