package com.meetingscheduler.controller;

import com.meetingscheduler.service.MailService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestEmailController {

    private final MailService mailService;

    public TestEmailController(MailService mailService) {
        this.mailService = mailService;
    }

    @GetMapping("/test-email")
    public String testEmail() {
        try {
            mailService.sendInvitation(
                    "your-other-email@example.com", // 👈 change this to test
                    "Test Email",
                    "This is a test email from Meeting Scheduler!"
            );
            return "✅ Test email sent!";
        } catch (Exception e) {
            return "❌ Failed: " + e.getMessage();
        }
    }
}
