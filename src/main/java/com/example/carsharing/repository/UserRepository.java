package com.example.carsharing.repository;

import com.example.carsharing.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    @Query("SELECT u FROM User u " +
            "WHERE u.email = :email " +
            "AND u.isDeleted = false")
    Optional<User> findUserByEmail(String email);
}
