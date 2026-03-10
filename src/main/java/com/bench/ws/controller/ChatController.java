package com.bench.ws.controller;

import java.time.LocalDateTime;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bench.ws.dto.CallSignal;
import com.bench.ws.dto.Message;
import com.bench.ws.repository.MessageRepository;

@Controller
public class ChatController {

    private final MessageRepository messageRepository;

    public ChatController(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }

    // ── Chat messages ──────────────────────────────────────────
    @MessageMapping("/send")
    @SendTo("/topic/channel1")
    public Message handleSendMessage(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return message;
    }

    // ── Call ended — save duration ─────────────────────────────
    @MessageMapping("/call-ended")
    @SendTo("/topic/call-ended")
    public Message handleCallEnded(Message message) {
        message.setTimestamp(LocalDateTime.now());
        messageRepository.save(message);
        return message;
    }

    // ── WebRTC signalling ──────────────────────────────────────
    @MessageMapping("/call-signal")
    @SendTo("/topic/call-signal")
    public CallSignal handleCallSignal(CallSignal signal) {
        return signal;
    }

    // ── Call ring / reject ─────────────────────────────────────
    @MessageMapping("/call-notify")
    @SendTo("/topic/call-notify")
    public CallSignal handleCallNotify(CallSignal signal) {
        return signal;
    }
}