package com.meetingscheduler.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Base64;
import java.util.Map;

@Service
public class ImageGenService {

    @Value("${bytez.api.key}")
    private String apiKey;

    private final WebClient webClient;

    public ImageGenService() {
        this.webClient = WebClient.builder()
                .baseUrl("https://api.bytez.ai/v1") // ✅ correct base
                .build();
    }

    public String generateImage(String prompt) {
        try {
            Map response = this.webClient.post()
                    .uri("/image/generations") // ✅ correct endpoint
                    .header("Authorization", "Bearer " + apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(Map.of(
                            "model", "dreamlike-art/dreamlike-diffusion-1.0",
                            "prompt", prompt
                    ))
                    .retrieve()
                    .bodyToMono(Map.class)
                    .block();

            System.out.println("Bytez raw response: " + response);

            if (response != null && response.get("data") != null) {
                // Bytez returns base64 image(s) in data[0]
                String base64 = ((java.util.List<String>) response.get("data")).get(0);
                return "data:image/png;base64," + base64;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}