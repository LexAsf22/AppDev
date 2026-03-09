package com.bench.ws.dto;

public class CallSignal {

    private String sender;
    private String type;    // "OFFER" | "ANSWER" | "ICE" | "END" | "RING"
    private String mode;    // "voice" | "video"  (used for RING)
    private String target;  // "all" or specific user (used for RING)
    private String payload; // JSON-stringified SDP or ICE candidate

    public CallSignal() {}

    public String getSender()           { return sender; }
    public void   setSender(String s)   { this.sender = s; }

    public String getType()             { return type; }
    public void   setType(String t)     { this.type = t; }

    public String getMode()             { return mode; }
    public void   setMode(String m)     { this.mode = m; }

    public String getTarget()           { return target; }
    public void   setTarget(String t)   { this.target = t; }

    public String getPayload()          { return payload; }
    public void   setPayload(String p)  { this.payload = p; }
}