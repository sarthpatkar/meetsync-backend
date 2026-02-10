package com.meetingscheduler.controller;

import com.meetingscheduler.service.ImageGenService;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class ImageGenController {

    private final ImageGenService imageGenService;

    public ImageGenController(ImageGenService imageGenService) {
        this.imageGenService = imageGenService;
    }

   @PostMapping("/generate-image")
public Map<String, Object> generateImage(@RequestBody Map<String, String> body) {
    String prompt = body.get("prompt");
    String image = imageGenService.generateImage(prompt);

    Map<String, Object> response = new HashMap<>();
    if (image != null) {
        response.put("url", image);  // ✅ match frontend expectation
    } else {
        response.put("error", "Failed to generate image");
    }
    return response;
}
}