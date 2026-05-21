package com.taskmanager.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AIServiceException extends RuntimeException {

    public AIServiceException(String message) {
        super(message);
        log.error("AI Service error: {}", message);
    }

    public AIServiceException(String message, Throwable cause) {
        super(message, cause);
        log.error("AI Service error: {} - Cause: {}", message, cause.getMessage());
    }

    public AIServiceException(Throwable cause) {
        super("AI Service is currently unavailable. Please try again later.", cause);
        log.error("AI Service unavailable: {}", cause.getMessage());
    }

    public AIServiceException(String serviceName, int statusCode) {
        super(String.format("AI Service '%s' returned error status code: %d", serviceName, statusCode));
        log.error("AI Service {} error - Status code: {}", serviceName, statusCode);
    }
}
