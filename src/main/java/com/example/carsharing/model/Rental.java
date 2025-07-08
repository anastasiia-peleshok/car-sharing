package com.example.carsharing.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "rentals")
@SQLDelete(sql = "UPDATE rentals SET is_deleted = TRUE, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
@AllArgsConstructor
@ToString(exclude = {"user", "car", "payment"})
@EqualsAndHashCode(callSuper = true, exclude = {"user", "car", "payment"})
public class Rental extends BaseEntity {
    @Column(name = "is_returned", columnDefinition = "TINYINT(1)", nullable = false)
    private boolean isReturned;

    @Column(name = "rental_start", nullable = false)
    private LocalDateTime rentalStart;

    @Column(name = "rental_end", nullable = false)
    private LocalDateTime rentalEnd;

    @Column(name = "actual_rental_end", nullable = false)
    private LocalDateTime actualRentalEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

}
