package com.bench.ws.service;

import java.util.Map;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Send notification to one authenticated user
    public void notifyUser(String username, String type, String message) {

        Map<String, Object> notification = Map.of(
                "type", type,
                "message", message,
                "timestamp", System.currentTimeMillis()
        );

        messagingTemplate.convertAndSendToUser(
                username,
                "/queue/notifications",
                (Object) notification
        );
    }

    // Broadcast notification to all users
    public void notifyAll(String type, String message) {

        Map<String, Object> notification = Map.of(
                "type", type,
                "message", message,
                "timestamp", System.currentTimeMillis()
        );

        messagingTemplate.convertAndSend(
                "/topic/notifications",
                (Object) notification
        );
    }
}