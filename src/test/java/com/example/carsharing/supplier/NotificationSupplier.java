package com.example.carsharing.supplier;


import com.example.carsharing.model.Notification;
import com.example.carsharing.model.NotificationSubject;
import com.example.carsharing.model.User;

import java.time.LocalDateTime;
import java.util.UUID;

public class NotificationSupplier {

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
    public static Notification getNotification() {
        Notification notification = new Notification();
        notification.setId(UUID.randomUUID());
        notification.setUser(user());
        notification.setSubject(NotificationSubject.BOOKING_CONFIRMED.toString());
        notification.setBody("Your booking is confirmed!");
        return notification;
    }
}