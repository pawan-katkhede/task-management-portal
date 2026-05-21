package com.taskmanager.model.enums;

public enum Priority {
    HIGH,
    MEDIUM,
    LOW;
    
    // Helper method to get priority level
    public int getLevel() {
        switch(this) {
            case HIGH: return 3;
            case MEDIUM: return 2;
            case LOW: return 1;
            default: return 0;
        }
    }
    
    // Helper method to check if priority is valid
    public static boolean isValid(String priority) {
        try {
            Priority.valueOf(priority.toUpperCase());
            return true;
        } catch (IllegalArgumentException | NullPointerException e) {
            return false;
        }
    }
}