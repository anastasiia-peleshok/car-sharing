package com.example.carsharing.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;

@Data
@Entity
@Table(name = "notifications")
@SQLDelete(sql = "UPDATE notifications SET is_deleted = TRUE, deleted_at = NOW() WHERE id = ?")
@SQLRestriction("is_deleted = FALSE")
@NoArgsConstructor
@ToString
@EqualsAndHashCode(callSuper = true)
public class Notification extends BaseEntity {
    @Column(nullable = false)
    String subject;
    @Column(nullable = false)
    String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    User user;
}
