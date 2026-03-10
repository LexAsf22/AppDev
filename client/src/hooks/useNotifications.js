// src/hooks/useNotifications.js
import { useState, useEffect } from "react";

export function useNotifications(stompClient, connected) {
  const [notifications, setNotifications] = useState([]);
  const [unreadCount, setUnreadCount] = useState(0);

  useEffect(() => {
    if (!connected || !stompClient) return;

    // Private notifications — only this user receives these
    const privateSub = stompClient.subscribe(
      "/user/queue/notifications",
      (frame) => {
        const notification = JSON.parse(frame.body);
        setNotifications(prev => [notification, ...prev]);
        setUnreadCount(prev => prev + 1);
      }
    );

    // Broadcast notifications — all users receive these
    const broadcastSub = stompClient.subscribe(
      "/topic/notifications",
      (frame) => {
        const notification = JSON.parse(frame.body);
        setNotifications(prev => [notification, ...prev]);
        setUnreadCount(prev => prev + 1);
      }
    );

    return () => {
      privateSub.unsubscribe();
      broadcastSub.unsubscribe();
    };
  }, [connected, stompClient]);

  const markAllRead = () => setUnreadCount(0);

  return { notifications, unreadCount, markAllRead };
}