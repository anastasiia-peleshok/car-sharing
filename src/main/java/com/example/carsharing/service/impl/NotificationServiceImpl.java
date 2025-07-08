package com.example.carsharing.service.impl;

import com.example.carsharing.model.Notification;
import com.example.carsharing.model.User;
import com.example.carsharing.repository.NotificationRepository;
import com.example.carsharing.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final JavaMailSender javaMailSender;
    private final NotificationRepository notificationRepository;
    @Value(value = "spring.mail.username")
    private String senderEmail;


    @Override
    @Transactional
    public boolean sendNotification(User user, String subject, String content) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(senderEmail);
        message.setTo(user.getEmail());
        message.setSubject(subject);
        message.setText(content);

        Notification notification = new Notification();
        notification.setUser(user);
        notification.setSubject(subject);
        notification.setBody(content);
        notification.setCreatedAt(LocalDateTime.now());
        notification.setUpdatedAt(LocalDateTime.now());
        notificationRepository.save(notification);

        javaMailSender.send(message);
        return true;

    }
}
