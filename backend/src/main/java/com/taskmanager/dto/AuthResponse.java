package com.taskmanager.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    private String token;
    
    private String email;
    
    private String message;
    
    private boolean success;
    
    // Constructor for successful login/registration
    public AuthResponse(String token, String email, String message) {
        this.token = token;
        this.email = email;
        this.message = message;
        this.success = true;
    }
    
    // Constructor for error response
    public AuthResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}