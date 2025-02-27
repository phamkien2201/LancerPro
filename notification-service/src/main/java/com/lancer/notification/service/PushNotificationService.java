package com.lancer.notification.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class PushNotificationService {
    public void sendNotification(String clientId, String subject, String body) {
        log.info("Sending push notification to client: {} - Subject: {} - Body: {}", clientId, subject, body);
        // Ở đây có thể tích hợp WebSocket hoặc Firebase Cloud Messaging (FCM)
    }
}
