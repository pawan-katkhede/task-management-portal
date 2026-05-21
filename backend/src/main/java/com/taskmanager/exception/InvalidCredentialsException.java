package com.taskmanager.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class InvalidCredentialsException extends RuntimeException {

    public InvalidCredentialsException(String message) {
        super(message);
        log.error("Invalid credentials: {}", message);
    }

    public InvalidCredentialsException() {
        super("Invalid email or password");
        log.error("Invalid credentials provided");
    }

    public InvalidCredentialsException(String email, String reason) {
        super(String.format("Invalid credentials for email: %s. Reason: %s", email, reason));
        log.error("Invalid credentials for email: {} - {}", email, reason);
    }
}