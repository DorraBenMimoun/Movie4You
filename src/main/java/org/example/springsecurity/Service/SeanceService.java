package org.example.springsecurity.Service;

import jakarta.validation.Valid;
import org.example.springsecurity.DTO.SeanceDTO;
import org.example.springsecurity.Models.CinemaEntity;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Repositories.CinemaRepository;
import org.example.springsecurity.Repositories.MovieRepository;
import org.example.springsecurity.Repositories.SeanceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class SeanceService {

    private final SeanceRepository seanceRepository;
    private final CinemaRepository cinemaRepository;
    private final MovieRepository movieRepository; // Ajout du repository Movie
    private static final Logger logger = Logger.getLogger(SeanceService.class.getName());

    @Autowired
    public SeanceService(SeanceRepository seanceRepository, CinemaRepository cinemaRepository, MovieRepository movieRepository) {
        this.seanceRepository = seanceRepository;
        this.cinemaRepository = cinemaRepository;
        this.movieRepository = movieRepository;
    }

    public SeanceEntity addSeance(SeanceDTO seanceDTO) {
        System.out.println("seance dto : "+seanceDTO.toString());
        logger.info("Creating a new seance with movie id: " + seanceDTO.getMovieId());

        // Vérification de l'existence du cinéma
        Optional<CinemaEntity> cinemaOptional = cinemaRepository.findById(seanceDTO.getCinemaId());
        if (cinemaOptional.isEmpty()) {
            String errorMessage = "Cinema with ID " + seanceDTO.getCinemaId() + " not found.";
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        CinemaEntity cinema = cinemaOptional.get();
        logger.info("Found cinema with ID: " + cinema.getId());

        // Vérification de l'existence du film
        Optional<MovieEntity> movieOptional = movieRepository.findById(seanceDTO.getMovieId());
        if (movieOptional.isEmpty()) {
            String errorMessage = "Movie with ID " + seanceDTO.getMovieId() + " not found.";
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage);
        }
        MovieEntity movie = movieOptional.get();
        logger.info("Found movie with ID: " + movie.getId());

        // Création de la séance
        SeanceEntity seance = new SeanceEntity();
        seance.setMovie(movie); // Utilisation de l'entité Movie
        seance.setStartTime(seanceDTO.getStartTime());
        seance.setDuration(seanceDTO.getDuration());
        seance.setCinema(cinema);
        seance.setDate(seanceDTO.getStartTime().toLocalDate()); // Définir la date de la séance

        try {
            SeanceEntity savedSeance = seanceRepository.save(seance);
            logger.info("Seance created successfully with ID: " + savedSeance.getId());
            return savedSeance;
        } catch (Exception e) {
            String errorMessage = "Error occurred while saving the seance: " + e.getMessage();
            logger.severe(errorMessage);
            throw new RuntimeException(errorMessage, e);
        }
    }

    public List<SeanceEntity> getSeancesByCinemaId(int cinemaId) {
        return seanceRepository.findByCinemaId(cinemaId);
    }

    public void deleteSeanceById(int id) {
        seanceRepository.deleteById(id);
    }

    public List<SeanceEntity> getAllSeances() {
        return seanceRepository.findAll();
    }

    public SeanceEntity getSeanceById(int id) {
        return seanceRepository.findById(id).orElseThrow(() -> new RuntimeException("Seance not found with id: " + id));
    }

    public SeanceEntity updateSeance(SeanceEntity seance) {
        return seanceRepository.save(seance);

    }

    public Optional<CinemaEntity> getCinemaById(Integer cinemaId) {
        return cinemaRepository.findById(cinemaId);
    }

    public Optional<MovieEntity> getMovieById(Integer movieId) {
        return movieRepository.findById(movieId);
    }
}
