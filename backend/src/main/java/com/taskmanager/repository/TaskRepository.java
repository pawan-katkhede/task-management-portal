package com.taskmanager.repository;

import com.taskmanager.model.Task;
import com.taskmanager.model.enums.Priority;
import com.taskmanager.model.enums.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    
    // ===== Basic Queries =====
    
    /**
     * Find all tasks for a specific user (ordered by creation date descending)
     * @param userId the user's ID
     * @return list of tasks
     */
    List<Task> findByUserIdOrderByCreatedAtDesc(Long userId);
    
    /**
     * Find task by ID and verify it belongs to user
     * @param id task ID
     * @param userId user ID
     * @return Optional containing task if found and belongs to user
     */
    Optional<Task> findByIdAndUserId(Long id, Long userId);
    
    // ===== Filter by Status =====
    
    /**
     * Find tasks by user ID and status
     * @param userId user ID
     * @param status task status (TODO, IN_PROGRESS, DONE)
     * @return list of tasks with given status
     */
    List<Task> findByUserIdAndStatusOrderByCreatedAtDesc(Long userId, TaskStatus status);
    
    /**
     * Count tasks by user and status
     * @param userId user ID
     * @param status task status
     * @return count of tasks
     */
    long countByUserIdAndStatus(Long userId, TaskStatus status);
    
    // ===== Filter by Priority =====
    
    /**
     * Find tasks by user ID and priority
     * @param userId user ID
     * @param priority task priority (HIGH, MEDIUM, LOW)
     * @return list of tasks with given priority
     */
    List<Task> findByUserIdAndPriorityOrderByCreatedAtDesc(Long userId, Priority priority);
    
    // ===== Filter by Due Date =====
    
    /**
     * Find overdue tasks (due date before today and not DONE)
     * @param userId user ID
     * @param today current date
     * @param notDone status not equal to DONE
     * @return list of overdue tasks
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.dueDate < :today AND t.status != :notDone ORDER BY t.dueDate ASC")
    List<Task> findOverdueTasks(@Param("userId") Long userId, @Param("today") LocalDate today, @Param("notDone") TaskStatus notDone);
    
    /**
     * Find tasks due today
     * @param userId user ID
     * @param today current date
     * @return list of tasks due today
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.dueDate = :today ORDER BY t.priority DESC")
    List<Task> findTasksDueToday(@Param("userId") Long userId, @Param("today") LocalDate today);
    
    /**
     * Find upcoming tasks (due date between today and endDate)
     * @param userId user ID
     * @param today current date
     * @param endDate end date range
     * @return list of upcoming tasks
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND t.dueDate BETWEEN :today AND :endDate AND t.status != :doneStatus ORDER BY t.dueDate ASC")
    List<Task> findUpcomingTasks(@Param("userId") Long userId, @Param("today") LocalDate today, @Param("endDate") LocalDate endDate, @Param("doneStatus") TaskStatus doneStatus);
    
    // ===== Delete Operations =====
    
    /**
     * Delete all tasks for a user (used when deleting user account)
     * @param userId user ID
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.user.id = :userId")
    void deleteByUserId(@Param("userId") Long userId);
    
    /**
     * Delete all completed tasks for a user
     * @param userId user ID
     * @param doneStatus DONE status
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Task t WHERE t.user.id = :userId AND t.status = :doneStatus")
    void deleteCompletedTasks(@Param("userId") Long userId, @Param("doneStatus") TaskStatus doneStatus);
    
    // ===== Statistics Queries =====
    
    /**
     * Get task count by priority for a user
     * @param userId user ID
     * @return list of Object[] where [0]=priority, [1]=count
     */
    @Query("SELECT t.priority, COUNT(t) FROM Task t WHERE t.user.id = :userId GROUP BY t.priority")
    List<Object[]> getTaskCountByPriority(@Param("userId") Long userId);
    
    /**
     * Get task count by status for a user
     * @param userId user ID
     * @return list of Object[] where [0]=status, [1]=count
     */
    @Query("SELECT t.status, COUNT(t) FROM Task t WHERE t.user.id = :userId GROUP BY t.status")
    List<Object[]> getTaskCountByStatus(@Param("userId") Long userId);
    
    /**
     * Search tasks by title or description (case insensitive)
     * @param userId user ID
     * @param keyword search keyword
     * @return list of matching tasks
     */
    @Query("SELECT t FROM Task t WHERE t.user.id = :userId AND (LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%'))) ORDER BY t.createdAt DESC")
    List<Task> searchTasks(@Param("userId") Long userId, @Param("keyword") String keyword);
}