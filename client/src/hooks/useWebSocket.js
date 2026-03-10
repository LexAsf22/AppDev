// src/hooks/useWebSocket.js
import { useEffect, useRef, useState } from "react";
import { Client } from "@stomp/stompjs";
import SockJS from "sockjs-client";

export function useWebSocket(token) {
  const [connected, setConnected] = useState(false);
  const clientRef = useRef(null);

  useEffect(() => {
    if (!token) return;  // do not connect if there is no token yet

    const client = new Client({
      webSocketFactory: () => new SockJS("http://localhost:8080/ws"),

      connectHeaders: {
        // The backend JwtChannelInterceptor reads this on the CONNECT frame
        Authorization: `Bearer ${token}`,
      },

      onConnect: () => {
        setConnected(true);
        console.log("WebSocket connected and authenticated");
      },

      onDisconnect: () => {
        setConnected(false);
      },

      onStompError: (frame) => {
        // Token expired or invalid — handle gracefully
        console.error("STOMP error:", frame.headers["message"]);
        if (frame.headers["message"]?.includes("JWT")) {
          // Redirect to login or refresh token
          console.warn("JWT rejected — redirecting to login");
        }
      },

      reconnectDelay: 5000,
    });

    client.activate();
    clientRef.current = client;

    return () => client.deactivate();
  }, [token]);  // reconnect if the token changes (e.g. after refresh)

  return { clientRef: clientRef.current, connected };
}