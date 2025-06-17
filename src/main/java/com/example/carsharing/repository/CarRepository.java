package com.example.carsharing.repository;

import com.example.carsharing.model.Car;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CarRepository extends JpaRepository<Car, UUID>, JpaSpecificationExecutor<Car>, CustomCarRepository {

    @Query("SELECT DISTINCT c FROM Car c " +
            "LEFT JOIN FETCH c.features f " +
            "WHERE c.id = :id AND c.isDeleted = false " +
            "AND (f IS NULL OR f.isDeleted = false)")
    Optional<Car> findByIdWithFeatures(@Param("id") UUID id);

    List<Car> findAllByIsAvailable(Boolean isAvailable);


    @Query("SELECT DISTINCT c FROM Car c " +
            "LEFT JOIN FETCH c.features f " +
            "LEFT JOIN FETCH c.rentals r " +
            "WHERE c.id = :id AND c.isDeleted = false " +
            "AND (f IS NULL OR f.isDeleted = false)" +
            "AND (r IS NULL OR f.isDeleted = false)")
    Optional<Car> findByIdWithAllRelations(@Param("id") UUID id);


}
