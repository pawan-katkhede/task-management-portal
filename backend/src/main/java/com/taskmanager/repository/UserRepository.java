package com.taskmanager.repository;

import com.taskmanager.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    /**
     * Find user by email (used for login and authentication)
     * @param email user's email address
     * @return Optional containing user if found
     */
    Optional<User> findByEmail(String email);

    /**
     * Check if user exists by email (used for registration validation)
     * @param email user's email address
     * @return true if user exists, false otherwise
     */
    boolean existsByEmail(String email);

    // ✅ CHANGED: Removed findIdByEmail — Spring Data JPA cannot derive this method
    // and it was causing startup warnings. findByEmail already covers this use case.
}