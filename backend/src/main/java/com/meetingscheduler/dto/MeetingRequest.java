package com.meetingscheduler.dto;

import java.time.LocalDateTime;
import java.util.List;
import com.fasterxml.jackson.annotation.JsonFormat;

public class MeetingRequest {
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")   // ✅ match frontend datetime-local
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")   // ✅ match frontend datetime-local
    private LocalDateTime endTime;

    private String location; // ✅ NEW field for meeting location/link

    private Long organizerId; // link to user

    // ✅ Emails of participants
    private List<String> participantEmails;

    // --- Getters & Setters ---
    public String getTitle() { return title; }
    public void setTitle(String t) { this.title = t; }

    public String getDescription() { return description; }
    public void setDescription(String d) { this.description = d; }

    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime s) { this.startTime = s; }

    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime e) { this.endTime = e; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public Long getOrganizerId() { return organizerId; }
    public void setOrganizerId(Long id) { this.organizerId = id; }

    public List<String> getParticipantEmails() { return participantEmails; }
    public void setParticipantEmails(List<String> participantEmails) { this.participantEmails = participantEmails; }
}
