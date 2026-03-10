// src/hooks/useDirectMessages.js
import { useState, useEffect } from "react";

export function useDirectMessages(stompClient, connected) {
  const [messages, setMessages] = useState([]);

  useEffect(() => {
    if (!connected || !stompClient) return;

    const sub = stompClient.subscribe(
      "/user/queue/messages",
      (frame) => {
        const msg = JSON.parse(frame.body);
        setMessages(prev => [...prev, msg]);
      }
    );

    return () => sub.unsubscribe();
  }, [connected, stompClient]);

  const sendMessage = (stompClient, recipient, content) => {
    stompClient?.publish({
      destination: "/app/private",
      // No need to send sender — backend reads it from JWT principal
      body: JSON.stringify({ recipient, content }),
    });
  };

  return { messages, sendMessage };
}