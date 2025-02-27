package com.lancer.notification.controller;

import com.lancer.notification.service.PushNotificationService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import com.lancer.event.dto.NotificationEvent;
import com.lancer.notification.dto.request.Recipient;
import com.lancer.notification.dto.request.SendEmailRequest;
import com.lancer.notification.service.EmailService;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class NotificationController {

    EmailService emailService;
    PushNotificationService pushNotificationService;

    @KafkaListener(topics = "register-successful")
    public void listenNotification(NotificationEvent message) {
        log.info("Message received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder().email(message.getRecipient()).build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }
    @KafkaListener(topics = "user-deleted")
    public void listenUserDeletionNotification(NotificationEvent message) {
        log.info("User Deletion Message received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder().email(message.getRecipient()).build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }

    @KafkaListener(topics = "user-status-changed")
    public void listenUserDeactivationNotification(NotificationEvent message) {
        log.info("User Deactivation Message received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder().email(message.getRecipient()).build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }

    @KafkaListener(topics = "bid-placed-email", groupId = "notification-group")
    public void handleBidEmailNotification(NotificationEvent message) {
        log.info("Bid Email Notification received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder().email(message.getRecipient()).build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }

    @KafkaListener(topics = "bid-placed-push", groupId = "notification-group")
    public void handleBidPushNotification(NotificationEvent message) {
        log.info("Bid Push Notification received: {}", message);
        pushNotificationService.sendNotification(message.getRecipient(), message.getSubject(), message.getBody());
    }

    @KafkaListener(topics = "bid-status-email", groupId = "notification-group")
    public void handleBidStatusEmail(NotificationEvent message) {
        log.info("Bid Status Email Notification received: {}", message);
        emailService.sendEmail(SendEmailRequest.builder()
                .to(Recipient.builder().email(message.getRecipient()).build())
                .subject(message.getSubject())
                .htmlContent(message.getBody())
                .build());
    }
    @KafkaListener(topics = "bid-status-push", groupId = "notification-group")
    public void handleBidStatusPush(NotificationEvent message) {
        log.info("Bid Status Push Notification received: {}", message);
        pushNotificationService.sendNotification(message.getRecipient(), message.getSubject(), message.getBody());
    }

}
