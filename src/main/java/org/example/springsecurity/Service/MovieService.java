package org.example.springsecurity.Service;


import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Repositories.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    @Autowired
    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    // Méthode pour ajouter un nouveau film
    public MovieEntity addMovie(MovieEntity movie) {
        return movieRepository.save(movie);
    }

    // Méthode pour obtenir tous les films
    public List<MovieEntity> getAllMovies() {
        return movieRepository.findAll();
    }

    // Méthode pour obtenir un film par son ID
    public Optional<MovieEntity> getMovieById(Integer id) {
        return movieRepository.findById(id);
    }

    // Méthode pour obtenir les films par leur nom (utilisé pour la recherche)
    public List<MovieEntity> getMoviesByName(String name) {
        return movieRepository.findByNameContainingIgnoreCase(name);
    }

    // Méthode pour supprimer un film par son ID
    public void deleteMovieById(Integer id) {
        movieRepository.deleteById(id);
    }

    // Méthode pour mettre à jour un film existant
    public MovieEntity updateMovie(MovieEntity movie) {
        return movieRepository.save(movie);
    }

    public boolean movieExists(int id) {
        return movieRepository.existsById(id);
    }

    public MovieEntity findById(Integer id) {return movieRepository.findById(id).get();}

    public void addMovie2(MovieEntity movie) {
        try {
            movieRepository.save(movie);
        } catch (Exception e) {
            System.err.println("Erreur lors de l'ajout du film : " + e.getMessage());
            throw e;
        }
    }

    public List<MovieEntity> getMoviesByIds(List<Integer> watchList) {return movieRepository.findAllById(watchList);}
    // Recherche de films par nom
    public List<MovieEntity> searchMoviesByName(String name) {
        return movieRepository.findByNameContainingIgnoreCase(name);
    }

}
