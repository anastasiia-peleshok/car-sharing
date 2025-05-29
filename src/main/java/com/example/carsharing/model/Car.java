package com.example.carsharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Table(name = "cars")
@SQLDelete(sql = "UPDATE cars SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
public class Car {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(columnDefinition = "BINARY(16)")
    private UUID id;
    @Column(nullable = false)
    private String brand;
    @Column(nullable = false)
    private String model;
    @Column(nullable = false)
    private String color;
    @Enumerated(EnumType.STRING)
    @Column(name = "car_type", nullable = false)
    private CarType carType;
    @Column(nullable = false)
    private int year;
    @Column(nullable = false)
    private BigDecimal price;
    @Column(nullable = false,  columnDefinition = "TINYINT(1)")
    private boolean isAvailable;

    @OneToMany(mappedBy = "car")
    private List<Rental> rentalList;

    @Column(name = "created_at", nullable = false)
    private LocalDate createdAt;
    @Column(name = "updated_at", nullable = false)
    private LocalDate updatedAt;
    @Column(name = "deleted_at")
    private LocalDate deletedAt;
    @Column(name = "is_deleted",  columnDefinition = "TINYINT(1)",  nullable = false)
    private boolean isDeleted;

}
