// src/hooks/useRoom.js
import { useState, useRef } from "react";

export function useRoom(stompClient, token) {
  const [roomMessages, setRoomMessages] = useState({});
  const [activeRooms, setActiveRooms] = useState([]);
  const subscriptionsRef = useRef({});

  const createRoom = async () => {
    const res = await fetch("http://localhost:8080/rooms/create", {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },  // JWT in REST call
    });
    const { roomId } = await res.json();
    subscribeToRoom(roomId);
    return roomId;
  };

  const joinRoom = async (roomId) => {
    await fetch(`http://localhost:8080/rooms/${roomId}/join`, {
      method: "POST",
      headers: { Authorization: `Bearer ${token}` },
    });
    subscribeToRoom(roomId);
  };

  const subscribeToRoom = (roomId) => {
    if (subscriptionsRef.current[roomId]) return;

    const sub = stompClient.subscribe(
      `/topic/room/${roomId}`,
      (frame) => {
        const msg = JSON.parse(frame.body);
        setRoomMessages(prev => ({
          ...prev,
          [roomId]: [...(prev[roomId] || []), msg],
        }));
      }
    );

    subscriptionsRef.current[roomId] = sub;
    setActiveRooms(prev => [...prev, roomId]);
  };

  const leaveRoom = (roomId) => {
    subscriptionsRef.current[roomId]?.unsubscribe();
    delete subscriptionsRef.current[roomId];
    setActiveRooms(prev => prev.filter(id => id !== roomId));
  };

  const sendToRoom = (roomId, content) => {
    stompClient?.publish({
      destination: `/app/room/${roomId}`,
      body: JSON.stringify({ content }),  // sender comes from JWT on backend
    });
  };

  return { roomMessages, activeRooms, createRoom, joinRoom, leaveRoom, sendToRoom };
}