package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AIResponse {
    
    private String description;
    
    private String priority;  // HIGH, MEDIUM, LOW
    
    private Integer estimatedHours;
    
    private boolean success;
    
    private String message;
    
    // Static factory method for successful AI response
    public static AIResponse success(String description, String priority, Integer estimatedHours) {
        return AIResponse.builder()
                .description(description)
                .priority(priority)
                .estimatedHours(estimatedHours)
                .success(true)
                .message("AI generated successfully")
                .build();
    }
    
    // Static factory method for fallback response
    public static AIResponse fallback(String taskTitle) {
        return AIResponse.builder()
                .description("AI service temporarily unavailable. Please complete this task: " + taskTitle)
                .priority("MEDIUM")
                .estimatedHours(2)
                .success(false)
                .message("Using fallback response. Gemini API may be unavailable.")
                .build();
    }
    
    // Static factory method for custom error response
    public static AIResponse error(String message) {
        return AIResponse.builder()
                .description("Unable to generate task details")
                .priority("MEDIUM")
                .estimatedHours(2)
                .success(false)
                .message(message)
                .build();
    }
}