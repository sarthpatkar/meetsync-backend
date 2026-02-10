package com.meetingscheduler.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.time.LocalDateTime;

public class MeetingResponse {
    private Long id;
    private String title;
    private String description;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")  // 👈 formatted for frontend
    private LocalDateTime startTime;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm")  // 👈 formatted for frontend
    private LocalDateTime endTime;

    private String organizerUsername;

    // ✅ NEW FIELD
    private String location;

    public MeetingResponse(Long id, String title, String desc,
                           LocalDateTime start, LocalDateTime end,
                           String organizerUsername, String location) {
        this.id = id;
        this.title = title;
        this.description = desc;
        this.startTime = start;
        this.endTime = end;
        this.organizerUsername = organizerUsername;
        this.location = location;
    }

    // getters
    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public LocalDateTime getStartTime() { return startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public String getOrganizerUsername() { return organizerUsername; }
    public String getLocation() { return location; }
}
