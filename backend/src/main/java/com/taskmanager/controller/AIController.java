package com.taskmanager.controller;

import com.taskmanager.dto.AIResponse;
import com.taskmanager.service.GeminiAIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
// ✅ CHANGED: Removed @CrossOrigin — CORS is now handled globally in SecurityConfig
public class AIController {

    private final GeminiAIService geminiAIService;

    @PostMapping("/generate")
    public ResponseEntity<AIResponse> generateTaskDetails(
            @RequestBody Map<String, String> request,
            @AuthenticationPrincipal UserDetails userDetails) {

        String taskTitle = request.get("title");
        log.info("AI generation request for task title: {} by user: {}", taskTitle, userDetails.getUsername());

        if (taskTitle == null || taskTitle.trim().isEmpty()) {
            log.warn("AI generation request with empty title");
            AIResponse errorResponse = AIResponse.builder()
                    .description("")
                    .priority("MEDIUM")
                    .estimatedHours(0)
                    .success(false)
                    .message("Task title is required")
                    .build();
            return ResponseEntity.badRequest().body(errorResponse);
        }

        AIResponse response = geminiAIService.generateTaskDetails(taskTitle);
        return ResponseEntity.ok(response);
    }
}