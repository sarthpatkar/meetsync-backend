package com.meetingscheduler.service;

import com.meetingscheduler.dto.MeetingRequest;
import com.meetingscheduler.exception.MeetingConflictException;
import com.meetingscheduler.model.Meeting;
import com.meetingscheduler.model.User;
import com.meetingscheduler.repository.MeetingRepository;
import com.meetingscheduler.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MeetingService {

    private final MeetingRepository meetingRepo;
    private final UserRepository userRepo;
    private final MailService mailService;

    public MeetingService(MeetingRepository meetingRepo, UserRepository userRepo, MailService mailService) {
        this.meetingRepo = meetingRepo;
        this.userRepo = userRepo;
        this.mailService = mailService;
    }

    // ✅ Create new meeting and send invitations
    public Meeting create(MeetingRequest req) {
        User organizer = userRepo.findById(req.getOrganizerId())
                .orElseThrow(() -> new RuntimeException("Organizer not found"));

        // ⚡ Conflict check
        boolean conflict = meetingRepo.existsByOrganizerAndTimeOverlap(
                organizer.getId(),
                req.getStartTime(),
                req.getEndTime(),
                -1L
        );
        if (conflict) {
            throw new MeetingConflictException("❌ Time conflict! Another meeting already exists in this slot.");
        }

        Meeting m = new Meeting();
        m.setTitle(req.getTitle());
        m.setDescription(req.getDescription());
        m.setStartTime(req.getStartTime());
        m.setEndTime(req.getEndTime());
        m.setLocation(req.getLocation()); // ✅ save location
        m.setOrganizer(organizer);

        Meeting savedMeeting = meetingRepo.save(m);

        // Send invitations
        if (req.getParticipantEmails() != null && !req.getParticipantEmails().isEmpty()) {
            String subject = "Invitation: " + savedMeeting.getTitle();
            String body = buildInvitationBody(savedMeeting);
            for (String to : req.getParticipantEmails()) {
                try {
                    mailService.sendInvitation(to, subject, body);
                } catch (Exception ex) {
                    System.err.println("❌ Failed to send invite to " + to + " : " + ex.getMessage());
                }
            }
        }

        return savedMeeting;
    }

    // ✅ Get all meetings
    public List<Meeting> getAll() {
        return meetingRepo.findAll();
    }

    // ✅ Get meetings for a specific user (organizer)
    public List<Meeting> getByUser(String username) {
        return meetingRepo.findByOrganizer_Username(username);
    }

    // ✅ Get meeting by ID
    public Meeting getById(Long id) {
        return meetingRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found"));
    }

    // ✅ Update meeting and optionally resend invitations
    public Meeting update(Long id, MeetingRequest req) {
        Meeting existing = getById(id);

        // ⚡ Conflict check
        boolean conflict = meetingRepo.existsByOrganizerAndTimeOverlap(
                existing.getOrganizer().getId(),
                req.getStartTime(),
                req.getEndTime(),
                id
        );
        if (conflict) {
            throw new MeetingConflictException("❌ Time conflict! Another meeting already exists in this slot.");
        }

        existing.setTitle(req.getTitle());
        existing.setDescription(req.getDescription());
        existing.setStartTime(req.getStartTime());
        existing.setEndTime(req.getEndTime());
        existing.setLocation(req.getLocation()); // ✅ update location

        if (req.getOrganizerId() != null) {
            User organizer = userRepo.findById(req.getOrganizerId())
                    .orElseThrow(() -> new RuntimeException("Organizer not found"));
            existing.setOrganizer(organizer);
        }

        Meeting updatedMeeting = meetingRepo.save(existing);

        // Resend invitations
        if (req.getParticipantEmails() != null && !req.getParticipantEmails().isEmpty()) {
            String subject = "Updated Invitation: " + updatedMeeting.getTitle();
            String body = buildInvitationBody(updatedMeeting);
            for (String to : req.getParticipantEmails()) {
                try {
                    mailService.sendInvitation(to, subject, body);
                } catch (Exception ex) {
                    System.err.println("❌ Failed to resend invite to " + to + " : " + ex.getMessage());
                }
            }
        }

        return updatedMeeting;
    }

    // ✅ Delete meeting
    public void delete(Long id) {
        if (!meetingRepo.existsById(id)) {
            throw new RuntimeException("Meeting not found");
        }
        meetingRepo.deleteById(id);
    }

    // ✅ Email body generator (with location)
    private String buildInvitationBody(Meeting meeting) {
        return "📅 You are invited to a meeting.\n\n"
                + "Title: " + meeting.getTitle() + "\n"
                + "Description: " + (meeting.getDescription() == null ? "" : meeting.getDescription()) + "\n"
                + "Start: " + meeting.getStartTime() + "\n"
                + "End: " + meeting.getEndTime() + "\n"
                + "Location: " + (meeting.getLocation() == null ? "N/A" : meeting.getLocation()) + "\n"
                + "Organizer: " + (meeting.getOrganizer() != null ? meeting.getOrganizer().getUsername() : "unknown");
    }
}