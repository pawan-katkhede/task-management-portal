package com.taskmanager.service;

import com.taskmanager.dto.AIResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
@RequiredArgsConstructor
public class GeminiAIService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${gemini.api.key:YOUR_GEMINI_API_KEY_PLACEHOLDER}")
    private String apiKey;

    @Value("${gemini.api.url:https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent}")
    private String apiUrl;

    private static final String FALLBACK_DESCRIPTION = "Complete this task as per requirements";
    private static final String FALLBACK_PRIORITY = "MEDIUM";
    private static final int FALLBACK_HOURS = 2;

    public AIResponse generateTaskDetails(String taskTitle) {
        log.info("Calling Gemini AI to generate task details for title: {}", taskTitle);

        try {
            // Check if API key is configured
            if (apiKey == null || apiKey.equals("YOUR_GEMINI_API_KEY_PLACEHOLDER")) {
                log.warn("Gemini API key not configured. Using fallback response.");
                return getFallbackResponse(taskTitle);
            }

            // Build request body
            String requestBody = buildRequestBody(taskTitle);
            
            // Set headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Create HTTP entity
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            
            // Call Gemini API
            String fullUrl = apiUrl + "?key=" + apiKey;
            ResponseEntity<String> response = restTemplate.exchange(
                fullUrl,
                HttpMethod.POST,
                entity,
                String.class
            );
            
            // Parse response
            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return parseResponse(response.getBody(), taskTitle);
            } else {
                log.error("Gemini API returned error status: {}", response.getStatusCode());
                return getFallbackResponse(taskTitle);
            }
            
        } catch (Exception e) {
            log.error("Error calling Gemini API: {}", e.getMessage());
            return getFallbackResponse(taskTitle);
        }
    }

    private String buildRequestBody(String taskTitle) {
        String prompt = String.format(
            "You are a task management assistant. For the task title: '%s', generate a JSON response with exactly these three fields: " +
            "description (a short detailed description of what needs to be done), " +
            "priority (must be one of: HIGH, MEDIUM, LOW), " +
            "estimatedHours (a number representing hours needed). " +
            "Return ONLY valid JSON in this format: {\"description\":\"...\", \"priority\":\"...\", \"estimatedHours\":...}",
            taskTitle
        );
        
        return String.format(
            "{\"contents\":[{\"parts\":[{\"text\":\"%s\"}]}]}",
            prompt.replace("\"", "\\\"").replace("\n", " ")
        );
    }

    private AIResponse parseResponse(String responseBody, String taskTitle) {
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            JsonNode textNode = root.path("candidates")
                .path(0)
                .path("content")
                .path("parts")
                .path(0)
                .path("text");
            
            if (textNode.isMissingNode()) {
                log.warn("Unexpected Gemini API response structure");
                return getFallbackResponse(taskTitle);
            }
            
            String aiText = textNode.asText();
            // Extract JSON from response (Gemini might add extra text)
            String jsonStr = extractJson(aiText);
            
            JsonNode aiResponse = objectMapper.readTree(jsonStr);
            
            String description = aiResponse.path("description").asText(FALLBACK_DESCRIPTION);
            String priority = aiResponse.path("priority").asText(FALLBACK_PRIORITY);
            int estimatedHours = aiResponse.path("estimatedHours").asInt(FALLBACK_HOURS);
            
            // Validate priority
            if (!isValidPriority(priority)) {
                priority = FALLBACK_PRIORITY;
            }
            
            return AIResponse.builder()
                    .description(description)
                    .priority(priority)
                    .estimatedHours(estimatedHours)
                    .success(true)
                    .message("AI generated successfully")
                    .build();
                    
        } catch (Exception e) {
            log.error("Failed to parse Gemini response: {}", e.getMessage());
            return getFallbackResponse(taskTitle);
        }
    }

    private String extractJson(String text) {
        int start = text.indexOf('{');
        int end = text.lastIndexOf('}');
        if (start != -1 && end != -1 && end > start) {
            return text.substring(start, end + 1);
        }
        return text;
    }

    private boolean isValidPriority(String priority) {
        return priority.equalsIgnoreCase("HIGH") || 
               priority.equalsIgnoreCase("MEDIUM") || 
               priority.equalsIgnoreCase("LOW");
    }

    private AIResponse getFallbackResponse(String taskTitle) {
        log.info("Using fallback response for task: {}", taskTitle);
        
        return AIResponse.builder()
                .description("Please complete: " + taskTitle + ". " + FALLBACK_DESCRIPTION)
                .priority(FALLBACK_PRIORITY)
                .estimatedHours(FALLBACK_HOURS)
                .success(false)
                .message("Using fallback response. Gemini API key may not be configured or service unavailable.")
                .build();
    }
}