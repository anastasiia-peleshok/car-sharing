package com.example.carsharing.repository;

import com.example.carsharing.model.Rental;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Repository
public interface RentalRepository extends JpaRepository<Rental, UUID> {
    @Query(value = "select * from rentals r where " +
            "r.acutal_rental_end iS null " +
            "AND DATE(r.end_date) <=DATE(:deadline)", nativeQuery = true)
    List<Rental> findAllOverdues(@Param("deadline") LocalDate deadline);

    List<Rental> findAllByUserId(UUID userId);
}
