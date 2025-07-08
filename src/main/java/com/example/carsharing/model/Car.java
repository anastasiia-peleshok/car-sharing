package com.example.carsharing.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.Where;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "cars")
@SQLDelete(sql = "UPDATE cars SET is_deleted = TRUE, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
@ToString(exclude = {"rentals", "features"})
@EqualsAndHashCode(callSuper = true, exclude = {"rentals", "features"})
public class Car extends BaseEntity {
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

    @Column(nullable = false, columnDefinition = "TINYINT(1)")
    private boolean isAvailable;

    @OneToMany(mappedBy = "car", fetch = FetchType.LAZY)
    @Setter(AccessLevel.PRIVATE)
    private Set<Rental> rentals = new HashSet<>();

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "car_feature",
            joinColumns = @JoinColumn(name = "car_id"),
            inverseJoinColumns = @JoinColumn(name = "feature_id")
    )
    @Setter(AccessLevel.PRIVATE)
    private Set<Feature> features =  new HashSet<>();
}
