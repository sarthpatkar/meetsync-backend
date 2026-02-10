package com.meetingscheduler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.util.*;

@Service
public class AIService {

    @Value("${GOOGLE_API_KEY}")
    private String apiKey;

    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent";

    public String getAIResponse(String message) {
        try {
            RestTemplate restTemplate = new RestTemplate();

            // ✅ Request body
            Map<String, Object> requestBody = new HashMap<>();
            Map<String, Object> parts = new HashMap<>();
            parts.put("text", message);
            Map<String, Object> content = new HashMap<>();
            content.put("parts", Collections.singletonList(parts));
            requestBody.put("contents", Collections.singletonList(content));

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    API_URL + "?key=" + apiKey,
                    HttpMethod.POST,
                    entity,
                    Map.class
            );

            // ✅ Extract AI response
            Map responseBody = response.getBody();
            if (responseBody != null) {
                List candidates = (List) responseBody.get("candidates");
                if (candidates != null && !candidates.isEmpty()) {
                    Map candidate = (Map) candidates.get(0);
                    Map contentMap = (Map) candidate.get("content");
                    List partsList = (List) contentMap.get("parts");
                    if (partsList != null && !partsList.isEmpty()) {
                        Map firstPart = (Map) partsList.get(0);
                        return (String) firstPart.get("text");
                    }
                }
            }
            return "⚠️ No response from AI";

        } catch (Exception e) {
            e.printStackTrace();
            return "❌ Error: Failed to contact AI service.";
        }
    }
}