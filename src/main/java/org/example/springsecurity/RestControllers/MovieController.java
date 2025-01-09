package org.example.springsecurity.RestControllers;

import jakarta.validation.Valid;

import org.example.springsecurity.DTO.MovieDTO;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Repositories.MovieRepository;
import org.example.springsecurity.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/movies")
public class MovieController {

    private final MovieService movieService;
    @Autowired
    MovieRepository movieRepository;

    @Autowired
    public MovieController(MovieService movieService) {
        this.movieService = movieService;
    }

    @PostMapping("/")
    public ResponseEntity<MovieEntity> createMovie(@Valid @RequestBody MovieDTO movieDTO) {
        MovieEntity movie = new MovieEntity();
        movie.setName(movieDTO.getName());
        movie.setSlug(movieDTO.getSlug());
        movie.setReleaseDate(movieDTO.getReleaseDate());
        movie.setDuration(movieDTO.getDuration());

        movie.setPosters(movieDTO.getPosters());
        movie.setGenre(movieDTO.getGenre());
        movie.setPlots(movieDTO.getPlots());
        movie.setActors(movieDTO.getActors());
        movie.setBandeAnnonce(movieDTO.getBandeAnnonce());
        movie.setRealisateur(movieDTO.getRealisateur());

        return ResponseEntity.ok(movieService.addMovie(movie));
    }

    @GetMapping("/movie_contain/{name}")
    public ResponseEntity<List<MovieEntity>> getMovie(@PathVariable("name") String name) {
        List<MovieEntity> movies = movieService.getMoviesByName(name);
        if (movies.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(movies);
    }
    @GetMapping("/{id}")
    public ResponseEntity<MovieEntity> getMovieById(@PathVariable int id) {
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if (!movie.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(movie.get());
    }


    @GetMapping("/")
    public ResponseEntity<List<MovieEntity>> getAllMovies() {
        List<MovieEntity> movies = movieService.getAllMovies();
        return ResponseEntity.ok(movies);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMovie(@PathVariable int id) {
        if (!movieRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        movieService.deleteMovieById(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<String> editMovie(
            @PathVariable("id") Integer id,
            @RequestBody MovieDTO movieDTO) {
        System.out.println("movie put"+movieDTO);
        Optional<MovieEntity> movieDb = movieRepository.findById(id);
        if (!movieDb.isPresent()) {
            return ResponseEntity.notFound().build();
        }

        MovieEntity movie = movieDb.get();
        System.out.println("movie: " + movie);

        // Mise à jour des champs
        if (movieDTO.getName() != null) movie.setName(movieDTO.getName());
        if (movieDTO.getSlug() != null) movie.setSlug(movieDTO.getSlug());
        if (movieDTO.getReleaseDate() != null) movie.setReleaseDate(movieDTO.getReleaseDate());
        if (movieDTO.getDuration() != null) movie.setDuration(movieDTO.getDuration());
        if (movieDTO.getPosters() != null) movie.setPosters(movieDTO.getPosters());
        if (movieDTO.getGenre() != null) movie.setGenre(movieDTO.getGenre());
        if (movieDTO.getPlots() != null) movie.setPlots(movieDTO.getPlots());
        if (movieDTO.getActors() != null) movie.setActors(movieDTO.getActors());
        if (movieDTO.getBandeAnnonce() != null) movie.setBandeAnnonce(movieDTO.getBandeAnnonce());
        if (movieDTO.getRealisateur() != null) movie.setRealisateur(movieDTO.getRealisateur());

        movieRepository.save(movie);

        return ResponseEntity.ok("Movie updated successfully: " + movie.getName());
    }


}
