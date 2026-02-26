import React, { useState, useEffect, useRef } from "react";
import SockJS from "sockjs-client";
import { Client } from "@stomp/stompjs";

const styles = `
  @import url('https://fonts.googleapis.com/css2?family=Syne:wght@400;700;800&family=DM+Mono:wght@300;400;500&display=swap');

  *, *::before, *::after { box-sizing: border-box; margin: 0; padding: 0; }

  :root {
    --bg: #0a0a0f;
    --surface: #111118;
    --surface2: #18181f;
    --border: #2a2a38;
    --accent: #e8ff47;
    --accent2: #ff4778;
    --text: #f0f0f5;
    --muted: #6b6b80;
    --own-bubble: #1e1e2e;
    --other-bubble: #16161e;
  }

  .chat-root {
    font-family: 'DM Mono', monospace;
    background: var(--bg);
    color: var(--text);
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    padding: 20px;
    position: relative;
    overflow: hidden;
  }

  .chat-root::before {
    content: '';
    position: fixed;
    inset: 0;
    background:
      radial-gradient(ellipse 60% 40% at 80% 10%, rgba(232,255,71,0.07) 0%, transparent 60%),
      radial-gradient(ellipse 50% 50% at 10% 90%, rgba(255,71,120,0.07) 0%, transparent 60%);
    pointer-events: none;
    z-index: 0;
  }

  /* ─── JOIN SCREEN ─── */
  .join-screen {
    position: relative;
    z-index: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 0;
    animation: fadeUp 0.6s cubic-bezier(0.16,1,0.3,1) both;
  }

  @keyframes fadeUp {
    from { opacity: 0; transform: translateY(30px); }
    to   { opacity: 1; transform: translateY(0); }
  }

  .join-label {
    font-family: 'Syne', sans-serif;
    font-weight: 800;
    font-size: clamp(2.5rem, 8vw, 5rem);
    letter-spacing: -0.03em;
    line-height: 0.95;
    text-transform: uppercase;
    color: var(--text);
    margin-bottom: 48px;
    text-align: center;
  }

  .join-label span {
    color: var(--accent);
    display: block;
  }

  .join-field-wrap {
    position: relative;
    width: min(380px, 90vw);
    margin-bottom: 16px;
  }

  .join-field-wrap::after {
    content: attr(data-char);
    position: absolute;
    right: 16px;
    top: 50%;
    transform: translateY(-50%);
    font-size: 12px;
    color: var(--muted);
    pointer-events: none;
  }

  .join-input {
    width: 100%;
    background: var(--surface2);
    border: 1px solid var(--border);
    border-radius: 12px;
    padding: 18px 20px;
    font-family: 'DM Mono', monospace;
    font-size: 15px;
    color: var(--text);
    outline: none;
    transition: border-color 0.2s, box-shadow 0.2s;
    letter-spacing: 0.02em;
  }

  .join-input::placeholder { color: var(--muted); }

  .join-input:focus {
    border-color: var(--accent);
    box-shadow: 0 0 0 3px rgba(232,255,71,0.12);
  }

  .join-btn {
    width: min(380px, 90vw);
    background: var(--accent);
    color: #0a0a0f;
    border: none;
    border-radius: 12px;
    padding: 18px;
    font-family: 'Syne', sans-serif;
    font-weight: 700;
    font-size: 15px;
    text-transform: uppercase;
    letter-spacing: 0.1em;
    cursor: pointer;
    transition: transform 0.15s, box-shadow 0.15s, opacity 0.15s;
  }

  .join-btn:hover:not(:disabled) {
    transform: translateY(-2px);
    box-shadow: 0 8px 32px rgba(232,255,71,0.35);
  }

  .join-btn:active:not(:disabled) { transform: translateY(0); }
  .join-btn:disabled { opacity: 0.4; cursor: not-allowed; }

  .chat-screen {
    position: relative;
    z-index: 1;
    width: min(520px, 95vw);
    display: flex;
    flex-direction: column;
    height: min(700px, 92vh);
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 24px;
    overflow: hidden;
    animation: fadeUp 0.5s cubic-bezier(0.16,1,0.3,1) both;
    box-shadow: 0 32px 80px rgba(0,0,0,0.6), 0 0 0 1px rgba(255,255,255,0.03);
  }

  /* header */
  .chat-header {
    padding: 20px 24px;
    border-bottom: 1px solid var(--border);
    display: flex;
    align-items: center;
    justify-content: space-between;
    flex-shrink: 0;
    background: var(--surface2);
  }

  .chat-header-left {
    display: flex;
    align-items: center;
    gap: 12px;
  }

  .status-dot {
    width: 8px;
    height: 8px;
    border-radius: 50%;
    background: var(--accent);
    box-shadow: 0 0 8px var(--accent);
    animation: pulse 2s ease-in-out infinite;
  }

  @keyframes pulse {
    0%, 100% { box-shadow: 0 0 6px var(--accent); }
    50% { box-shadow: 0 0 14px var(--accent), 0 0 28px rgba(232,255,71,0.3); }
  }

  .chat-header-title {
    font-family: 'Syne', sans-serif;
    font-weight: 700;
    font-size: 15px;
    letter-spacing: 0.05em;
    text-transform: uppercase;
  }

  .chat-header-sub {
    font-size: 11px;
    color: var(--muted);
    letter-spacing: 0.05em;
    margin-top: 2px;
  }

  .user-pill {
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 20px;
    padding: 6px 14px;
    font-size: 12px;
    color: var(--accent);
    letter-spacing: 0.06em;
  }

  /* messages */
  .messages-area {
    flex: 1;
    overflow-y: auto;
    padding: 24px;
    display: flex;
    flex-direction: column;
    gap: 4px;
    scroll-behavior: smooth;
  }

  .messages-area::-webkit-scrollbar { width: 4px; }
  .messages-area::-webkit-scrollbar-track { background: transparent; }
  .messages-area::-webkit-scrollbar-thumb { background: var(--border); border-radius: 4px; }

  .msg-row {
    display: flex;
    flex-direction: column;
    animation: msgIn 0.25s cubic-bezier(0.34,1.56,0.64,1) both;
  }

  @keyframes msgIn {
    from { opacity: 0; transform: scale(0.9) translateY(8px); }
    to   { opacity: 1; transform: scale(1) translateY(0); }
  }

  .msg-row.own { align-items: flex-end; }
  .msg-row.other { align-items: flex-start; }

  .msg-sender {
    font-size: 10px;
    color: var(--muted);
    letter-spacing: 0.08em;
    text-transform: uppercase;
    padding: 0 12px;
    margin-bottom: 4px;
    margin-top: 12px;
  }

  .msg-bubble {
    max-width: 78%;
    padding: 12px 16px;
    border-radius: 18px;
    font-size: 14px;
    line-height: 1.55;
    letter-spacing: 0.01em;
    word-break: break-word;
  }

  .msg-row.own .msg-bubble {
    background: linear-gradient(135deg, #e8ff47 0%, #d4f520 100%);
    color: #0a0a0f;
    border-bottom-right-radius: 4px;
    font-weight: 500;
  }

  .msg-row.other .msg-bubble {
    background: var(--surface2);
    border: 1px solid var(--border);
    color: var(--text);
    border-bottom-left-radius: 4px;
  }

  .empty-state {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 12px;
    color: var(--muted);
    font-size: 13px;
    letter-spacing: 0.04em;
  }

  .empty-icon {
    font-size: 32px;
    opacity: 0.4;
  }

  /* input bar */
  .input-bar {
    padding: 16px 20px;
    border-top: 1px solid var(--border);
    display: flex;
    gap: 10px;
    align-items: flex-end;
    background: var(--surface2);
    flex-shrink: 0;
  }

  .msg-input-wrap {
    flex: 1;
    position: relative;
  }

  .msg-input {
    width: 100%;
    background: var(--surface);
    border: 1px solid var(--border);
    border-radius: 14px;
    padding: 13px 16px;
    font-family: 'DM Mono', monospace;
    font-size: 14px;
    color: var(--text);
    outline: none;
    resize: none;
    transition: border-color 0.2s, box-shadow 0.2s;
    line-height: 1.5;
    max-height: 120px;
    overflow-y: auto;
  }

  .msg-input::placeholder { color: var(--muted); }

  .msg-input:focus {
    border-color: rgba(232,255,71,0.5);
    box-shadow: 0 0 0 3px rgba(232,255,71,0.08);
  }

  .send-btn {
    width: 46px;
    height: 46px;
    border-radius: 14px;
    border: none;
    background: var(--accent);
    color: #0a0a0f;
    cursor: pointer;
    display: flex;
    align-items: center;
    justify-content: center;
    flex-shrink: 0;
    transition: transform 0.15s, box-shadow 0.15s, opacity 0.15s;
  }

  .send-btn:hover:not(:disabled) {
    transform: scale(1.08);
    box-shadow: 0 6px 24px rgba(232,255,71,0.4);
  }

  .send-btn:active:not(:disabled) { transform: scale(0.96); }
  .send-btn:disabled { opacity: 0.3; cursor: not-allowed; }

  .send-btn svg { width: 18px; height: 18px; }
`;

let prevSender = null;

export default function Chat() {
  const [name, setName] = useState("");
  const [joined, setJoined] = useState(false);
  const [message, setMessage] = useState("");
  const [messages, setMessages] = useState([]);
  const stompClient = useRef(null);
  const messagesEndRef = useRef(null);
  const inputRef = useRef(null);

  const scrollToBottom = () => {
    messagesEndRef.current?.scrollIntoView({ behavior: "smooth" });
  };

  useEffect(() => { scrollToBottom(); }, [messages]);

  const connect = () => {
    const socket = new SockJS("http://localhost:8080/ws");
    const client = new Client({
      webSocketFactory: () => socket,
      reconnectDelay: 5000,
      onConnect: () => {
        client.subscribe("/topic/channel1", (response) => {
          const receivedMessage = JSON.parse(response.body);
          setMessages((prev) => [...prev, receivedMessage]);
        });
      },
      onStompError: (frame) => {
        console.error("Broker error: ", frame.headers["message"]);
      },
    });
    client.activate();
    stompClient.current = client;
  };

  const joinChat = () => {
    if (name.trim()) {
      connect();
      setJoined(true);
    }
  };

  const sendMessage = () => {
    if (message.trim() && stompClient.current) {
      stompClient.current.publish({
        destination: "/app/send",
        body: JSON.stringify({ sender: name, content: message }),
      });
      setMessage("");
      inputRef.current?.focus();
    }
  };

  const handleKeyDown = (e) => {
    if (e.key === "Enter" && !e.shiftKey) {
      e.preventDefault();
      sendMessage();
    }
  };

  const handleJoinKey = (e) => {
    if (e.key === "Enter") joinChat();
  };

  useEffect(() => {
    return () => { stompClient.current?.deactivate(); };
  }, []);

  return (
    <>
      <style>{styles}</style>
      <div className="chat-root">
        {!joined ? (
          <div className="join-screen">
            <h1 className="join-label">
              Enter the<span>channel.</span>
            </h1>
            <div className="join-field-wrap">
              <input
                className="join-input"
                type="text"
                placeholder="your handle"
                value={name}
                onChange={(e) => setName(e.target.value)}
                onKeyDown={handleJoinKey}
                maxLength={24}
                autoFocus
              />
            </div>
            <button
              className="join-btn"
              onClick={joinChat}
              disabled={!name.trim()}
            >
              Join Channel 1 →
            </button>
          </div>
        ) : (
          <div className="chat-screen">
            <div className="chat-header">
              <div className="chat-header-left">
                <div className="status-dot" />
                <div>
                  <div className="chat-header-title">Channel 1</div>
                  <div className="chat-header-sub">{messages.length} messages</div>
                </div>
              </div>
              <div className="user-pill">{name}</div>
            </div>

            <div className="messages-area">
              {messages.length === 0 && (
                <div className="empty-state">
                  <div className="empty-icon">◎</div>
                  <span>no messages yet — say something</span>
                </div>
              )}
              {messages.map((msg, index) => {
                const isOwn = msg.sender === name;
                const showSender =
                  index === 0 || messages[index - 1].sender !== msg.sender;
                return (
                  <div key={index} className={`msg-row ${isOwn ? "own" : "other"}`}>
                    {showSender && !isOwn && (
                      <div className="msg-sender">{msg.sender}</div>
                    )}
                    {showSender && isOwn && (
                      <div className="msg-sender">you</div>
                    )}
                    <div className="msg-bubble">{msg.content}</div>
                  </div>
                );
              })}
              <div ref={messagesEndRef} />
            </div>

            <div className="input-bar">
              <div className="msg-input-wrap">
                <textarea
                  ref={inputRef}
                  className="msg-input"
                  placeholder="type a message..."
                  value={message}
                  onChange={(e) => setMessage(e.target.value)}
                  onKeyDown={handleKeyDown}
                  rows={1}
                />
              </div>
              <button
                className="send-btn"
                onClick={sendMessage}
                disabled={!message.trim()}
              >
                <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="2.5" strokeLinecap="round" strokeLinejoin="round">
                  <line x1="22" y1="2" x2="11" y2="13" />
                  <polygon points="22 2 15 22 11 13 2 9 22 2" />
                </svg>
              </button>
            </div>
          </div>
        )}
      </div>
    </>
  );
}