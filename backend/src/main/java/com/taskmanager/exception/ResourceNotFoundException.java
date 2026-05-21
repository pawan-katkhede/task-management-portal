package com.taskmanager.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
        log.error("Resource not found: {}", message);
    }

    public ResourceNotFoundException(String resourceName, Long id) {
        super(String.format("%s not found with id: %d", resourceName, id));
        log.error("{} not found with id: {}", resourceName, id);
    }

    public ResourceNotFoundException(String resourceName, String email) {
        super(String.format("%s not found with email: %s", resourceName, email));
        log.error("{} not found with email: {}", resourceName, email);
    }
}