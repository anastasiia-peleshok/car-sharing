package com.example.carsharing.supplier;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.Status;
import com.example.carsharing.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class PaymentSupplier {

    public static User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setDeletedAt(null);
        return user;
    }

    public static Rental rental() {
        Rental rental = new Rental();
        rental.setId(UUID.randomUUID());
        rental.setRentalStart(LocalDateTime.now());
        return rental;
    }

    public static Payment unpaidPayment() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(200));
        payment.setStatus(Status.UNPAID);
        payment.setUser(user());
        payment.setRental(rental());
        payment.setCreatedAt(LocalDateTime.now());
        payment.setUpdatedAt(LocalDateTime.now());
        payment.setDeleted(false);
        return payment;
    }

    public static Payment paymentWithNullAmount() {
        Payment payment = unpaidPayment();
        payment.setAmount(null);
        return payment;
    }

    public static Payment paymentWithNullUser() {
        Payment payment = unpaidPayment();
        payment.setUser(null);
        return payment;
    }

    public static Payment paymentWithNullRental() {
        Payment payment = unpaidPayment();
        payment.setRental(null);
        return payment;
    }
}
