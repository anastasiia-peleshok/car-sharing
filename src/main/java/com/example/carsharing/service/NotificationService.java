package com.example.carsharing.service;

import com.example.carsharing.dto.notification.NotificationDto;
import com.example.carsharing.model.User;

import java.util.UUID;

public interface NotificationService {
    boolean sendNotification(User user, String subject, String content);
}
