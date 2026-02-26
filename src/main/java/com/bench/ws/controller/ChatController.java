package com.bench.ws.controller;


import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


import com.bench.ws.dto.Message;


@Controller
public class ChatController {
   
    @MessageMapping("/send")
    @SendTo("/topic/channel1")
    public Message handleSendMessage(Message message) {
        return message;
    }
}
