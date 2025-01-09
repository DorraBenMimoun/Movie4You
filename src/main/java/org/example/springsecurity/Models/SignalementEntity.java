package org.example.springsecurity.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "signalements")
public class SignalementEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "raison")
    private String raison;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "status", nullable = false)
    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonBackReference("user-signalements")
    private User user;

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    @JsonBackReference("review-signalements")
    private ReviewEntity review;
}

