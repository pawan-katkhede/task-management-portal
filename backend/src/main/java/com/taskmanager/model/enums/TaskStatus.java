package com.taskmanager.model.enums;

public enum TaskStatus {
    TODO,
    IN_PROGRESS,
    DONE;
    
    // Helper method to get display name
    public String getDisplayName() {
        switch(this) {
            case TODO: return "To Do";
            case IN_PROGRESS: return "In Progress";
            case DONE: return "Done";
            default: return this.name();
        }
    }
    
    // Helper method to get next status (for workflow)
    public TaskStatus getNextStatus() {
        switch(this) {
            case TODO: return IN_PROGRESS;
            case IN_PROGRESS: return DONE;
            case DONE: return DONE;
            default: return this;
        }
    }
    
    // Helper method to check if status is valid
    public static boolean isValid(String status) {
        try {
            TaskStatus.valueOf(status.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
    
    // Helper method to get all statuses as array
    public static TaskStatus[] getAllStatuses() {
        return values();
    }
}