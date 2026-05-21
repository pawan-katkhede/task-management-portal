package com.taskmanager.controller;

import com.taskmanager.dto.TaskRequest;
import com.taskmanager.dto.TaskResponse;
import com.taskmanager.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
// ✅ CHANGED: Removed @CrossOrigin — CORS is now handled globally in SecurityConfig
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    public ResponseEntity<TaskResponse> createTask(
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Create task request for user: {}", userDetails.getUsername());

        TaskResponse response = taskService.createTask(request, userDetails.getUsername());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TaskResponse> updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Update task request for task id: {} by user: {}", id, userDetails.getUsername());

        TaskResponse response = taskService.updateTask(id, request, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Delete task request for task id: {} by user: {}", id, userDetails.getUsername());

        taskService.deleteTask(id, userDetails.getUsername());
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<TaskResponse> updateTaskStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.info("Update status request for task id: {} to {} by user: {}", id, status, userDetails.getUsername());

        TaskResponse response = taskService.updateTaskStatus(id, status, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TaskResponse> getTaskById(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Get task by id request for task id: {} by user: {}", id, userDetails.getUsername());

        TaskResponse response = taskService.getTaskById(id, userDetails.getUsername());
        return ResponseEntity.ok(response);
    }

    @GetMapping
    public ResponseEntity<List<TaskResponse>> getAllTasks(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Get all tasks request for user: {}", userDetails.getUsername());

        List<TaskResponse> tasks = taskService.getAllTasks(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<TaskResponse>> getTasksByStatus(
            @PathVariable String status,
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Get tasks by status request for user: {} with status: {}", userDetails.getUsername(), status);

        List<TaskResponse> tasks = taskService.getTasksByStatus(status, userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<TaskResponse>> getOverdueTasks(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Get overdue tasks request for user: {}", userDetails.getUsername());

        List<TaskResponse> tasks = taskService.getOverdueTasks(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }

    @GetMapping("/due-today")
    public ResponseEntity<List<TaskResponse>> getTasksDueToday(
            @AuthenticationPrincipal UserDetails userDetails) {

        log.debug("Get tasks due today request for user: {}", userDetails.getUsername());

        List<TaskResponse> tasks = taskService.getTasksDueToday(userDetails.getUsername());
        return ResponseEntity.ok(tasks);
    }
}