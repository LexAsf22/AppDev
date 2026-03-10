package com.bench.ws.controller;

import java.security.Principal;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.bench.ws.dto.DirectMessage;

@Controller
public class DirectMessageController {

    private final SimpMessagingTemplate messagingTemplate;

    public DirectMessageController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @MessageMapping("/private")
    public void handleDirectMessage(@Payload DirectMessage message, Principal principal) {

        // Stamp sender from authenticated user
        message.setSender(principal.getName());

        // Send message to recipient
        messagingTemplate.convertAndSendToUser(
                message.getRecipient(),
                "/queue/messages",
                message
        );

        // Send message back to sender
        messagingTemplate.convertAndSendToUser(
                principal.getName(),
                "/queue/messages",
                message
        );
    }
}