package org.example.springsecurity.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.DecimalMax;
import lombok.Data;

@Data
public class MovieDTO {

    @NotNull(message = "Name is required")
    @Size(min = 1, max = 100, message = "Name should be between 1 and 100 characters")
    private String name;

    @NotNull(message = "Slug is required")
    @Size(min = 1, max = 100, message = "Slug should be between 1 and 100 characters")
    private String slug;

    @NotNull(message = "Release date is required")
    @Size(min = 8, message = "Release date should be in the format YYYY-MM-DD")
    private String releaseDate;

    @NotNull(message = "Duration is required")
    @DecimalMin(value = "1", message = "Duration should be greater than 0")
    private Integer duration;


    @NotNull(message = "Posters URL is required")
    private String posters;

    @NotNull(message = "Genre is required")
    private String genre;

    @NotNull(message = "Plot is required")
    private String plots;

    @NotNull(message = "Actors are required")
    private String actors;

    @NotNull(message = "Trailer URL is required")
    private String bandeAnnonce;

    private String realisateur; // Optional field
}
