package com.bench.ws.dto;

public class Message {

    private String sender;
    private String content;
    private String type;    // "TEXT" | "IMAGE" | "FILE" | "AUDIO"
    private String fileUrl;

    // ---- constructors ----

    public Message() {}

    public Message(String sender, String content, String type, String fileUrl) {
        this.sender  = sender;
        this.content = content;
        this.type    = type;
        this.fileUrl = fileUrl;
    }

    // ---- getters & setters ----

    public String getSender()             { return sender; }
    public void   setSender(String s)     { this.sender = s; }

    public String getContent()            { return content; }
    public void   setContent(String c)    { this.content = c; }

    public String getType()               { return type; }
    public void   setType(String t)       { this.type = t; }

    public String getFileUrl()            { return fileUrl; }
    public void   setFileUrl(String u)    { this.fileUrl = u; }
}