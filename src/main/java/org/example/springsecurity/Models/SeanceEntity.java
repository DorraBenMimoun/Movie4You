package org.example.springsecurity.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

@Entity
@Data
@Table(name = "seances")
@Getter
@Setter
public class SeanceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;


    @Column(name = "date", nullable = false)
    private LocalDate date;


    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "duration", nullable = false)
    private int duration; // Duration in minutes

    @ManyToOne
    @JoinColumn(name = "cinema_id", nullable = false)
    @JsonBackReference
    private CinemaEntity cinema;

    // Association Many-to-One avec Movie
    @ManyToOne
    @JoinColumn(name = "movie_id", nullable = false)
    private MovieEntity movie;



}
