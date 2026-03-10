package com.bench.ws.service;

import org.springframework.stereotype.Service;

@Service
public class OrderService {

    private final NotificationService notificationService;

    public OrderService(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    public void shipOrder(String orderId, String username) {
        // shipping logic here

        notificationService.notifyUser(
                username,
                "ORDER_SHIPPED",
                "Your order #" + orderId + " has shipped!"
        );
    }
}