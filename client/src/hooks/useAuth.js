// src/hooks/useAuth.js
import { useState } from "react";

export function useAuth() {
  const [token, setToken] = useState(null);
  const [username, setUsername] = useState(null);
  const [error, setError] = useState(null);

  const login = async (username, password) => {
    try {
      const res = await fetch("http://localhost:8080/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password }),
      });

      if (!res.ok) {
        setError("Invalid credentials");
        return false;
      }

      const data = await res.json();
      setToken(data.token);
      setUsername(data.username);
      setError(null);
      return true;
    } catch (e) {
      setError("Connection failed");
      return false;
    }
  };

  const logout = () => {
    setToken(null);
    setUsername(null);
  };

  return { token, username, error, login, logout };
}