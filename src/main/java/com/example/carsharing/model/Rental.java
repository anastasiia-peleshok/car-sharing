package com.example.carsharing.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "rentals")
@SQLDelete(sql = "UPDATE rentals SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
@AllArgsConstructor
public class Rental {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(name = "is_returned",  columnDefinition = "TINYINT(1)",  nullable = false)
    private boolean isReturned;

    @Column(name = "rental_start", nullable = false)
    private LocalDate rentalStart;

    @Column(name = "rental_end", nullable = false)
    private LocalDate rentalEnd;

    @Column(name = "actual_rental_end", nullable = false)
    private LocalDate actualRentalEnd;

    @ManyToOne()
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToOne
    @JoinColumn(name = "payment_id", nullable = true)
    private Payment paymant;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;
    @Column(name = "deleted_at")
    private LocalDate deletedAt;
    @Column(name = "is_deleted",  columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isDeleted;

}
