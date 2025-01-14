package org.example.springsecurity.Models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "movies")
@Getter

@Setter
public class MovieEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column
    private String name;

    @Column
    private String slug;

    @Column
    private String releaseDate;

    @Column
    private Integer duration;



    @Column
    private String posters;

    @Column
    private String wallpaper;

    @Column
    private String genre;


    // Taille max 5000 characters
    @Column (length = 5000)
    private String plots;

    @Column
    private String actors;

    @Column
    private String bandeAnnonce;

    @Column
    private String realisateur;

    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ReviewEntity> reviews = new ArrayList<>();

    // Relation One-to-Many avec SeanceEntity
    @OneToMany(mappedBy = "movie", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<SeanceEntity> seances = new ArrayList<>();

}