package org.example.springsecurity.RestControllers;


import org.example.springsecurity.DTO.SeanceDTO;
import org.example.springsecurity.Models.CinemaEntity;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/seances")
public class SeanceController {

    private final SeanceService seanceService;

    @Autowired
    public SeanceController(SeanceService seanceService) {
        this.seanceService = seanceService;
    }

    @PostMapping("/")
    public ResponseEntity<SeanceEntity> createSeance(@Valid @RequestBody SeanceDTO seanceDTO) {
        SeanceEntity seance = seanceService.addSeance(seanceDTO);
        return ResponseEntity.ok(seance);
    }
    @GetMapping("/{id}")
    public ResponseEntity<SeanceEntity> getSeanceById(@PathVariable int id) {
        SeanceEntity seance = seanceService.getSeanceById(id);
        if (seance == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(seance);
    }

    @GetMapping("/by_cinema/{cinemaId}")
    public ResponseEntity<List<SeanceEntity>> getSeancesByCinemaId(@PathVariable int cinemaId) {
        List<SeanceEntity> seances = seanceService.getSeancesByCinemaId(cinemaId);
        return ResponseEntity.ok(seances);
    }


    @ControllerAdvice
    public class GlobalExceptionHandler {

        @ExceptionHandler(RuntimeException.class)
        public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
            // Retourner un message d'erreur explicite en cas de RuntimeException
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + ex.getMessage());
        }
    }
    @GetMapping("/")
    public ResponseEntity<List<SeanceEntity>> getAllSeances() {
        List<SeanceEntity> seances = seanceService.getAllSeances();
        return ResponseEntity.ok(seances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> editSeance(@PathVariable("id") Integer id, @RequestBody SeanceDTO seanceDTO) {
        // Récupérer la séance existante
        SeanceEntity seance = seanceService.getSeanceById(id);
        if (seance == null) {
            return ResponseEntity.notFound().build();
        }

        // Mise à jour des champs en utilisant le DTO
        if (seanceDTO.getDate() != null) {
            seance.setDate(seanceDTO.getDate()); // Convertir la chaîne en LocalDate
        }
        if (seanceDTO.getStartTime() != null) {
            seance.setStartTime(seanceDTO.getStartTime()); // Convertir la chaîne en LocalDateTime
        }
        if (seanceDTO.getDuration() != null) {
            seance.setDuration(seanceDTO.getDuration());
        }
        if (seanceDTO.getCinemaId() != null) {
            Optional<CinemaEntity> cinema = seanceService.getCinemaById(seanceDTO.getCinemaId());
            cinema.ifPresent(seance::setCinema);
        }
        if (seanceDTO.getMovieId() != null) {
            Optional<MovieEntity> movie = seanceService.getMovieById(seanceDTO.getMovieId());
            movie.ifPresent(seance::setMovie);
        }

        // Sauvegarde des modifications
        seanceService.updateSeance(seance);
        return ResponseEntity.ok("Seance updated successfully: ID " + seance.getId());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSeance(@PathVariable int id) {
        seanceService.deleteSeanceById(id);
        return ResponseEntity.ok("Seance deleted successfully");
    }
}