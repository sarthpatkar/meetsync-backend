package com.meetingscheduler.controller;

import com.meetingscheduler.dto.MeetingRequest;
import com.meetingscheduler.dto.MeetingResponse;
import com.meetingscheduler.model.Meeting;
import com.meetingscheduler.service.MeetingService;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/meetings")
public class MeetingController {
    private final MeetingService meetingService;

    public MeetingController(MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // ✅ Create meeting
    @PostMapping
    public MeetingResponse createMeeting(@RequestBody MeetingRequest req) {
        Meeting saved = meetingService.create(req);
        return mapToResponse(saved);
    }

    // ✅ Get ONLY meetings of the logged-in user
    @GetMapping("/my")
    public List<MeetingResponse> getMyMeetings(Authentication auth) {
        String username = auth.getName(); // 👈 Extract from JWT
        return meetingService.getByUser(username).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // ✅ Get meeting by ID
    @GetMapping("/{id}")
    public MeetingResponse getMeetingById(@PathVariable Long id) {
        Meeting m = meetingService.getById(id);
        return mapToResponse(m);
    }

    // ✅ Update meeting
    @PutMapping("/{id}")
    public MeetingResponse updateMeeting(@PathVariable Long id, @RequestBody MeetingRequest req) {
        Meeting updated = meetingService.update(id, req);
        return mapToResponse(updated);
    }

    // ✅ Delete meeting
    @DeleteMapping("/{id}")
    public void deleteMeeting(@PathVariable Long id) {
        meetingService.delete(id);
    }

    // ✅ Helper method → Entity to DTO
    private MeetingResponse mapToResponse(Meeting m) {
        return new MeetingResponse(
                m.getId(),
                m.getTitle(),
                m.getDescription(),
                m.getStartTime(),
                m.getEndTime(),
                m.getOrganizer() != null ? m.getOrganizer().getUsername() : "N/A",
                m.getLocation() != null ? m.getLocation() : "N/A" // 👈 Added location
        );
    }
}
