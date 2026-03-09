package com.bench.ws.example;

import java.util.Scanner;

import org.springframework.boot.CommandLineRunner;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

import com.bench.ws.dto.Message;

@Component
public class SimulateWs implements CommandLineRunner {

    private final SimpMessagingTemplate messagingTemplate;

    public SimulateWs(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @Override
    public void run(String... args) {

        // Run on a separate thread so the main Spring context
        // is not blocked during startup
        Thread simulatorThread = new Thread(() -> {

            try (Scanner sc = new Scanner(System.in)) {

                System.out.println("WebSocket Simulator**********************");
                System.out.println("Type a message and press Enter to send...");

                while (sc.hasNextLine()) {

                    String input = sc.nextLine().trim();
                    if (input.isBlank()) continue;

                    // FIX: constructor is (sender, content, type, fileUrl)
                    // "SERVER" is the sender, input is the content
                    Message msg = new Message("SERVER", input, "TEXT", null);
                    messagingTemplate.convertAndSend("/topic/channel1", msg);

                    System.out.println("Server sent: " + input);
                }
            }

        }, "ws-simulator");

        simulatorThread.setDaemon(true); // won't prevent app shutdown
        simulatorThread.start();
    }
}