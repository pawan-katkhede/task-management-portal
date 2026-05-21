package com.taskmanager.service;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.model.Task;
import com.taskmanager.model.User;
import com.taskmanager.model.enums.Priority;
import com.taskmanager.model.enums.TaskStatus;
import com.taskmanager.repository.TaskRepository;
import com.taskmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    // Create new task
    public TaskResponse createTask(TaskRequest request, String userEmail) {
        log.info("Creating task for user: {}", userEmail);
        
        User user = getUserByEmail(userEmail);
        
        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .priority(getPriorityFromString(request.getPriority()))
                .status(TaskStatus.TODO)
                .dueDate(request.getDueDate())
                .user(user)
                .build();
        
        Task savedTask = taskRepository.save(task);
        log.info("Task created successfully with id: {}", savedTask.getId());
        
        return TaskResponse.fromEntity(savedTask);
    }

    // Update existing task
    public TaskResponse updateTask(Long id, TaskRequest request, String userEmail) {
        log.info("Updating task with id: {} for user: {}", id, userEmail);
        
        Task task = getTaskByIdAndUserEmail(id, userEmail);
        
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        if (request.getPriority() != null) {
            task.setPriority(getPriorityFromString(request.getPriority()));
        }
        if (request.getStatus() != null) {
            task.setStatus(getStatusFromString(request.getStatus()));
        }
        task.setDueDate(request.getDueDate());
        
        Task updatedTask = taskRepository.save(task);
        log.info("Task updated successfully with id: {}", updatedTask.getId());
        
        return TaskResponse.fromEntity(updatedTask);
    }

    // Delete task
    public void deleteTask(Long id, String userEmail) {
        log.info("Deleting task with id: {} for user: {}", id, userEmail);
        
        Task task = getTaskByIdAndUserEmail(id, userEmail);
        taskRepository.delete(task);
        log.info("Task deleted successfully with id: {}", id);
    }

    // Update task status only
    public TaskResponse updateTaskStatus(Long id, String status, String userEmail) {
        log.info("Updating status for task id: {} to {} for user: {}", id, status, userEmail);
        
        Task task = getTaskByIdAndUserEmail(id, userEmail);
        task.setStatus(getStatusFromString(status));
        
        Task updatedTask = taskRepository.save(task);
        log.info("Status updated successfully for task id: {}", id);
        
        return TaskResponse.fromEntity(updatedTask);
    }

    // Get single task by id
    public TaskResponse getTaskById(Long id, String userEmail) {
        log.debug("Fetching task with id: {} for user: {}", id, userEmail);
        
        Task task = getTaskByIdAndUserEmail(id, userEmail);
        return TaskResponse.fromEntity(task);
    }

    // Get all tasks for a user
    public List<TaskResponse> getAllTasks(String userEmail) {
        log.debug("Fetching all tasks for user: {}", userEmail);
        
        User user = getUserByEmail(userEmail);
        List<Task> tasks = taskRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Get tasks by status
    public List<TaskResponse> getTasksByStatus(String status, String userEmail) {
        log.debug("Fetching tasks with status: {} for user: {}", status, userEmail);
        
        User user = getUserByEmail(userEmail);
        TaskStatus taskStatus = getStatusFromString(status);
        List<Task> tasks = taskRepository.findByUserIdAndStatusOrderByCreatedAtDesc(user.getId(), taskStatus);
        
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Get overdue tasks
    public List<TaskResponse> getOverdueTasks(String userEmail) {
        log.debug("Fetching overdue tasks for user: {}", userEmail);
        
        User user = getUserByEmail(userEmail);
        List<Task> tasks = taskRepository.findOverdueTasks(user.getId(), LocalDate.now(), TaskStatus.DONE);
        
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Get tasks due today
    public List<TaskResponse> getTasksDueToday(String userEmail) {
        log.debug("Fetching tasks due today for user: {}", userEmail);
        
        User user = getUserByEmail(userEmail);
        List<Task> tasks = taskRepository.findTasksDueToday(user.getId(), LocalDate.now());
        
        return tasks.stream()
                .map(TaskResponse::fromEntity)
                .collect(Collectors.toList());
    }

    // Helper method to get user by email
    private User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    // Helper method to get task by id and verify ownership
    private Task getTaskByIdAndUserEmail(Long id, String userEmail) {
        User user = getUserByEmail(userEmail);
        return taskRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Task not found with id: " + id));
    }

    // Helper method to convert string to Priority enum
    private Priority getPriorityFromString(String priority) {
        if (priority == null) return Priority.MEDIUM;
        try {
            return Priority.valueOf(priority.toUpperCase());
        } catch (IllegalArgumentException e) {
            return Priority.MEDIUM;
        }
    }

    // Helper method to convert string to TaskStatus enum
    private TaskStatus getStatusFromString(String status) {
        if (status == null) return TaskStatus.TODO;
        try {
            return TaskStatus.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            return TaskStatus.TODO;
        }
    }
}