package com.example.carsharing.repository;

import com.example.carsharing.model.Car;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, CustomCarRepository {
    @Query("SELECT DISTINCT c FROM Car c " +
            "LEFT JOIN FETCH c.features f " +
            "WHERE c.id = :id AND c.isDeleted = false " +
            "AND (f IS NULL OR f.isDeleted = false)")
    Optional<Car> findByIdWithFeatures(@Param("id") UUID id);

    @Query("SELECT DISTINCT c FROM Car c " +
            "LEFT JOIN FETCH c.rentals f " +
            "WHERE c.id = :id AND c.isDeleted = false " +
            "AND (f IS NULL OR f.isDeleted = false)")
    Optional<Car> findByIdWithRentals(@Param("id") UUID id);

    Page<Car> findAllByIsAvailable(Boolean isAvailable, Pageable pageable);

}
