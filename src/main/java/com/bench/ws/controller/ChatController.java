package com.bench.ws.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

import com.bench.ws.dto.Message;
import com.bench.ws.dto.CallSignal;

@Controller
public class ChatController {

    // ── Chat messages ──────────────────────────────────────────
    @MessageMapping("/send")
    @SendTo("/topic/channel1")
    public Message handleSendMessage(Message message) {
        return message;
    }

    // ── WebRTC SDP / ICE signalling (offer, answer, candidate) ─
    @MessageMapping("/call-signal")
    @SendTo("/topic/call-signal")
    public CallSignal handleCallSignal(CallSignal signal) {
        return signal;
    }

    // ── Call ring / reject notifications ───────────────────────
    @MessageMapping("/call-notify")
    @SendTo("/topic/call-notify")
    public CallSignal handleCallNotify(CallSignal signal) {
        return signal;
    }
}