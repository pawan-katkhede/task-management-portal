package com.taskmanager.dto;

import com.taskmanager.model.Task;
import com.taskmanager.model.enums.Priority;
import com.taskmanager.model.enums.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TaskResponse {
    
    private Long id;
    
    private String title;
    
    private String description;
    
    private String priority;
    
    private String status;
    
    private LocalDate dueDate;
    
    private LocalDateTime createdAt;
    
    private LocalDateTime updatedAt;
    
    private boolean overdue;
    
    private boolean completed;
    
    private String priorityColor;
    
    // Static method to convert Task entity to TaskResponse DTO
    public static TaskResponse fromEntity(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .priority(task.getPriority() != null ? task.getPriority().name() : "MEDIUM")
                .status(task.getStatus() != null ? task.getStatus().name() : "TODO")
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .overdue(task.isOverdue())
                .completed(task.isCompleted())
                .priorityColor(task.getPriorityColor())
                .build();
    }
}
