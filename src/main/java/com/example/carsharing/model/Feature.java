package com.example.carsharing.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "features")
@SQLDelete(sql = "UPDATE cars SET is_deleted = TRUE WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Feature extends BaseEntity {

    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
}
