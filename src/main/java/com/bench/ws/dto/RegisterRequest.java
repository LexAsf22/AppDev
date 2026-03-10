package com.bench.ws.dto;

public class RegisterRequest {
    private String username;
    private String password;

    public RegisterRequest() {}

    public String getUsername()           { return username; }
    public void setUsername(String u)     { this.username = u; }
    public String getPassword()           { return password; }
    public void setPassword(String p)     { this.password = p; }
}