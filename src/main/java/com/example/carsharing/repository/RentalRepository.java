package com.example.carsharing.repository;

import com.example.carsharing.model.Rental;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID> {
    @Query("SELECT DISTINCT r FROM Rental r " +
            "LEFT JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.car " +
            "WHERE r.actualRentalEnd IS NULL " +
            "AND r.rentalEnd <= :deadline " +
            "AND r.isDeleted = false")
    Page<Rental> findAllOverdues(@Param("deadline") LocalDateTime deadline, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Rental r " +
            "LEFT JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.car " +
            "WHERE r.user.id = :userId " +
            "AND r.isDeleted = false")
    Page<Rental> findAllByUserId(@Param("userId") UUID userId, Pageable pageable);

    @Query("SELECT DISTINCT r FROM Rental r " +
            "LEFT JOIN FETCH r.user " +
            "LEFT JOIN FETCH r.car " +
            "WHERE r.id = :id " +
            "AND r.isDeleted = false")
    Optional<Rental> findById(@Param("id") UUID id);

}
