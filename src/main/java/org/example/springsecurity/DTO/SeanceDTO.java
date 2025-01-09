package org.example.springsecurity.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class SeanceDTO {

    // Champ utilisé pour référencer le film via son ID
    @NotNull(message = "Movie ID cannot be null")
    private Integer movieId; // Assurez-vous que c'est du même type que l'ID de votre film

    @NotNull(message = "Start time cannot be null")
    private LocalDateTime startTime;

    @NotNull(message = "Duration cannot be null")
    private Integer duration;

    @NotNull(message = "Cinema ID cannot be null")
    private Integer cinemaId;

    @NotNull(message="Start Date")
    private LocalDate Date;
}
