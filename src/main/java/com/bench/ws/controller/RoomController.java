package com.bench.ws.controller;

import java.security.Principal;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RoomController {

    private final SimpMessagingTemplate messagingTemplate;

    // roomId -> members
    private final Map<String, Set<String>> roomMembers = new ConcurrentHashMap<>();

    public RoomController(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    // Create a room
    @PostMapping("/rooms/create")
    public Map<String, String> createRoom(Principal principal) {

        String roomId = UUID.randomUUID().toString().substring(0, 8);

        Set<String> members = new HashSet<>();
        members.add(principal.getName());

        roomMembers.put(roomId, members);

        Map<String, String> response = new HashMap<>();
        response.put("roomId", roomId);

        return response;
    }

    // Join a room
    @PostMapping("/rooms/{roomId}/join")
    public ResponseEntity<Void> joinRoom(
            @PathVariable String roomId,
            Principal principal
    ) {

        if (!roomMembers.containsKey(roomId)) {
            return ResponseEntity.notFound().build();
        }

        roomMembers.get(roomId).add(principal.getName());

        return ResponseEntity.ok().build();
    }

    // WebSocket room messaging
    @MessageMapping("/room/{roomId}")
    public void handleRoomMessage(
            @DestinationVariable String roomId,
            @Payload Map<String, String> payload,
            Principal principal
    ) {

        Set<String> members = roomMembers.get(roomId);

        if (members == null || !members.contains(principal.getName())) {
            throw new AccessDeniedException("Not a member of room: " + roomId);
        }

        Map<String, Object> message = new HashMap<>();
        message.put("sender", principal.getName());
        message.put("content", payload.get("content"));
        message.put("roomId", roomId);
        message.put("timestamp", System.currentTimeMillis());

        String destination = "/topic/room/" + roomId;

messagingTemplate.convertAndSend(destination, (Object) message);    }
}