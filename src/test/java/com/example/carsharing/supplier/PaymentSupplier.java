package com.example.carsharing.supplier;

import com.example.carsharing.model.Payment;
import com.example.carsharing.model.Rental;
import com.example.carsharing.model.Status;
import com.example.carsharing.model.User;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.UUID;

public class PaymentSupplier {

    public static User user() {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@example.com");
        user.setDeleted(false);
        user.setCreatedAt(LocalDate.now());
        user.setUpdatedAt(LocalDate.now());
        user.setDeletedAt(null); // optional, can leave as null
        return user;
    }

    public static Rental rental() {
        Rental rental = new Rental();
        rental.setId(UUID.randomUUID());
        rental.setRentalStart(LocalDate.now());
        return rental;
    }

    public static Payment unpaidPayment() {
        Payment payment = new Payment();
        payment.setId(UUID.randomUUID());
        payment.setAmount(BigDecimal.valueOf(200));
        payment.setStatus(Status.UNPAID);
        payment.setUser(user());
        payment.setRental(rental());
        payment.setCreatedAt(LocalDate.now());
        payment.setUpdatedAt(LocalDate.now());
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
