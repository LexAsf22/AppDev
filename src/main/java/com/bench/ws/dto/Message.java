package com.bench.ws.dto;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "messages")
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Column(columnDefinition = "TEXT")
    private String content;

    private String type;

    @Column(columnDefinition = "TEXT")
    private String fileUrl;

    private LocalDateTime timestamp;

    // For call history
    private Integer callDuration; // seconds, null if not a call

    public Message() {}

    public Message(String sender, String content, String type, String fileUrl) {
        this.sender    = sender;
        this.content   = content;
        this.type      = type;
        this.fileUrl   = fileUrl;
        this.timestamp = LocalDateTime.now();
    }

    public Long getId()                          { return id; }
    public String getSender()                    { return sender; }
    public void setSender(String s)              { this.sender = s; }
    public String getContent()                   { return content; }
    public void setContent(String c)             { this.content = c; }
    public String getType()                      { return type; }
    public void setType(String t)                { this.type = t; }
    public String getFileUrl()                   { return fileUrl; }
    public void setFileUrl(String u)             { this.fileUrl = u; }
    public LocalDateTime getTimestamp()          { return timestamp; }
    public void setTimestamp(LocalDateTime t)    { this.timestamp = t; }
    public Integer getCallDuration()             { return callDuration; }
    public void setCallDuration(Integer d)       { this.callDuration = d; }
}