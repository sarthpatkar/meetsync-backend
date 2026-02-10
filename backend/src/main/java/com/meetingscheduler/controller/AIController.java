package com.meetingscheduler.controller;

import com.meetingscheduler.dto.AIRequest;
import com.meetingscheduler.dto.AIResponse;
import com.meetingscheduler.service.AIService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
public class AIController {

    @Autowired
    private AIService aiService;

    @PostMapping("/chat")
    public AIResponse chat(@RequestBody AIRequest request) {
        String reply = aiService.getAIResponse(request.getMessage());
        return new AIResponse(reply);
    }
}