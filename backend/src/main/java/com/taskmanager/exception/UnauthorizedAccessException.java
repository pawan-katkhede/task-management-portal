package com.taskmanager.exception;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UnauthorizedAccessException extends RuntimeException {

    public UnauthorizedAccessException(String message) {
        super(message);
        log.error("Unauthorized access: {}", message);
    }

    public UnauthorizedAccessException(Long taskId, String userEmail) {
        super(String.format("User '%s' is not authorized to access task with id: %d", userEmail, taskId));
        log.error("Unauthorized access - User: {} attempted to access task: {}", userEmail, taskId);
    }

    public UnauthorizedAccessException(String resourceName, Long resourceId, String userEmail) {
        super(String.format("User '%s' is not authorized to access %s with id: %d", userEmail, resourceName, resourceId));
        log.error("Unauthorized access - User: {} attempted to access {}: {}", userEmail, resourceName, resourceId);
    }

    public UnauthorizedAccessException() {
        super("You do not have permission to perform this action");
        log.error("Unauthorized access attempt");
    }
}
