package com.taskmanager.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {
    
    private LocalDateTime timestamp;
    
    private int status;
    
    private String error;
    
    private String message;
    
    private String path;
    
    @Builder.Default
    private List<String> errors = new ArrayList<>();
    
    // Constructor for simple error response
    public ErrorResponse(int status, String error, String message, String path) {
        this.timestamp = LocalDateTime.now();
        this.status = status;
        this.error = error;
        this.message = message;
        this.path = path;
        this.errors = new ArrayList<>();
    }
    
    // Static factory method for 400 Bad Request
    public static ErrorResponse badRequest(String message, String path) {
        return new ErrorResponse(400, "Bad Request", message, path);
    }
    
    // Static factory method for 401 Unauthorized
    public static ErrorResponse unauthorized(String message, String path) {
        return new ErrorResponse(401, "Unauthorized", message, path);
    }
    
    // Static factory method for 403 Forbidden
    public static ErrorResponse forbidden(String message, String path) {
        return new ErrorResponse(403, "Forbidden", message, path);
    }
    
    // Static factory method for 404 Not Found
    public static ErrorResponse notFound(String message, String path) {
        return new ErrorResponse(404, "Not Found", message, path);
    }
    
    // Static factory method for 409 Conflict
    public static ErrorResponse conflict(String message, String path) {
        return new ErrorResponse(409, "Conflict", message, path);
    }
    
    // Static factory method for 500 Internal Server Error
    public static ErrorResponse internalError(String message, String path) {
        return new ErrorResponse(500, "Internal Server Error", message, path);
    }
}