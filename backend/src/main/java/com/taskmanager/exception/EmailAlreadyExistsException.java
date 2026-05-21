package com.taskmanager.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class EmailAlreadyExistsException extends RuntimeException {

    public EmailAlreadyExistsException(String message) {
        super(message);
        log.error("Email already exists: {}", message);
    }

    public EmailAlreadyExistsException(String email, String message) {
        super(String.format("Email '%s' already exists. %s", email, message));
        log.error("Email already exists: {} - {}", email, message);
    }

    public EmailAlreadyExistsException(String email, boolean isRegistration) {
        super(String.format("User with email '%s' already exists", email));
        log.error("Registration failed - Email already exists: {}", email);
    }
}