package org.example.springsecurity.Controllers;


import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.MovieRepository;
import org.example.springsecurity.Repositories.ReviewRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.example.springsecurity.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/movie")
public class Movie_mvc {

    @Autowired
    private MovieService movieService;

    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/all")
    public String getAllMovies(Model model) {
        List<MovieEntity> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        return "movie/list";
    }

    @GetMapping("/create")
    public String showCreateMovieForm(Model model) {
        model.addAttribute("movie", new MovieEntity());
        return "movie/create";
    }

    @PostMapping("/create")
    public String createMovie(@ModelAttribute MovieEntity movieInput, Model model,@RequestParam("posterFile") MultipartFile posterFile) {

        if (!posterFile.isEmpty()) {
            // Obtenir le chemin absolu du projet
            String projectDir = System.getProperty("user.dir");
            // Chemin vers le dossier uploads dans src/main/java/org/example/springsecurity/uploads
            String uploadDir = projectDir + "/src/main/resources/static/uploads/";
            String fileName = System.currentTimeMillis() + "_" + posterFile.getOriginalFilename(); // Nom unique
            File file = new File(uploadDir + fileName);

            // Créer le dossier si nécessaire
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try {
                posterFile.transferTo(file);

                movieInput.setPosters( fileName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        movieRepository.save(movieInput);
        return "redirect:/movie/all";
    }



    @GetMapping("/{id}")
    public String showMovieDetails(@PathVariable("id") Integer id, Model model) {
        Optional<MovieEntity> movieOptional = movieRepository.findById(id);
        if (movieOptional.isPresent()) {
            System.out.println("posters"+movieOptional.get().getPosters());
            model.addAttribute("movie", movieOptional.get());
            model.addAttribute("users", userRepository.findAll());

            return "movie/details";
        }
        model.addAttribute("error", "Movie not found!");

        return "error/404";
    }

    @GetMapping("/edit/{id}")
    public String showEditMovieForm(@PathVariable("id") Integer id, Model model) {
        Optional<MovieEntity> movie = movieRepository.findById(id);
        if (!movie.isPresent()) {
            return "error/404";
        }
        model.addAttribute("movie", movie.get());
        return "movie/edit";
    }

    @PostMapping("/edit/{id}")
    public String editMovie(@PathVariable("id") Integer id, @ModelAttribute MovieEntity movieInput, @RequestParam(value = "posterFile", required = false) MultipartFile posterFile, Model model) {
        Optional<MovieEntity> optionalMovie = movieRepository.findById(id);
        if (!optionalMovie.isPresent()) {
            model.addAttribute("error", "Movie not found!");
            return "error/404";
        }

        MovieEntity existingMovie = optionalMovie.get();

        // Si un nouveau fichier est téléchargé, on le remplace
        if (posterFile != null && !posterFile.isEmpty()) {
            // Obtenir le chemin absolu du projet
            String projectDir = System.getProperty("user.dir");
            String uploadDir = projectDir + "/src/main/resources/static/uploads/";
            String fileName = System.currentTimeMillis() + "_" + posterFile.getOriginalFilename(); // Nom unique
            File file = new File(uploadDir + fileName);

            // Créer le dossier si nécessaire
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }

            try {
                posterFile.transferTo(file);
                existingMovie.setPosters(fileName); // Mise à jour du nom du fichier
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        // Mise à jour des autres champs
        updateMovieFields(existingMovie, movieInput);
        movieRepository.save(existingMovie);

        return "redirect:/movie/all";
    }


    @GetMapping("/delete/{id}")
    public String deleteMovie(@PathVariable("id") Integer id, Model model) {
        if (!movieRepository.existsById(id)) {
            model.addAttribute("error", "Movie not found!");
            return "error/404";
        }

        movieService.deleteMovieById(id);
        return "redirect:/movie/all";
    }

    private void updateMovieFields(MovieEntity existingMovie, MovieEntity movieInput) {
        if (movieInput.getName() != null && !movieInput.getName().trim().isEmpty())
            existingMovie.setName(movieInput.getName());
        if (movieInput.getReleaseDate() != null)
            existingMovie.setReleaseDate(movieInput.getReleaseDate());


        if (movieInput.getPosters() != null)
            existingMovie.setPosters(movieInput.getPosters());
        if (movieInput.getGenre() != null)
            existingMovie.setGenre(movieInput.getGenre());
        if (movieInput.getPlots() != null)
            existingMovie.setPlots(movieInput.getPlots());
        if (movieInput.getActors() != null)
            existingMovie.setActors(movieInput.getActors());
        if (movieInput.getBandeAnnonce() != null)
            existingMovie.setBandeAnnonce(movieInput.getBandeAnnonce());
        if (movieInput.getRealisateur() != null)
            existingMovie.setRealisateur(movieInput.getRealisateur());
    }


}
