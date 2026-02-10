package com.meetingscheduler.dto;

public class AIResponse {
    private String reply;

    public AIResponse(String reply) {
        this.reply = reply;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }
}