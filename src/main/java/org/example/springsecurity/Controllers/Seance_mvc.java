package org.example.springsecurity.Controllers;

import org.example.springsecurity.DTO.SeanceDTO;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Repositories.CinemaRepository;
import org.example.springsecurity.Repositories.SeanceRepository;
import org.example.springsecurity.Service.MovieService;
import org.example.springsecurity.Service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/seance")
public class Seance_mvc {

    @Autowired
    private SeanceService seanceService;
    @Autowired
    private SeanceRepository seanceRepository;
    @Autowired
    private MovieService movieService;
    @Autowired
    private CinemaRepository cinemaRepository;

    @GetMapping("/all")
    public String getAllSeances(Model model) {
        List<SeanceEntity> seances = seanceRepository.findAll();
        model.addAttribute("seances", seances);
        return "seance/seances";
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("seanceDTO", new SeanceDTO());
        model.addAttribute("cinemas", cinemaRepository.findAll());
        model.addAttribute("movies", movieService.getAllMovies());  // Liste des films à afficher dans le select
        return "seance/create-seance";
    }

    @PostMapping("/create")
    public String createSeance(@ModelAttribute SeanceDTO seanceDTO) {
        System.out.println("movie id : "+seanceDTO.getMovieId());
        // Récupérer l'entité Movie à partir de movieId
        Optional<MovieEntity> movie = movieService.getMovieById(seanceDTO.getMovieId());
        if (movie == null) {
            // Si le film n'existe pas, rediriger vers une page d'erreur ou une page de création
            return "error/404";
        }

        // Création de la nouvelle séance avec le MovieEntity récupéré
        SeanceEntity seance = new SeanceEntity();
        seance.setMovie(movie.get()); // Associer le film à la séance
        seance.setStartTime(seanceDTO.getStartTime());
        seance.setDuration(seanceDTO.getDuration());
        seance.setCinema(cinemaRepository.findById(seanceDTO.getCinemaId()).orElseThrow(() -> new RuntimeException("Cinema not found")));

        seanceService.addSeance(seanceDTO);
        return "redirect:/seance/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable int id, Model model) {
        Optional<SeanceEntity> seanceOptional = seanceRepository.findById(id);
        if (!seanceOptional.isPresent()) {
            return "error/404"; // Page d'erreur si la séance n'existe pas
        }
        model.addAttribute("seance", seanceOptional.get());
        model.addAttribute("cinemas", cinemaRepository.findAll());
        model.addAttribute("movies", movieService.getAllMovies()); // Liste des films pour l'édition
        return "seance/edit-seance";
    }

    @PostMapping("/edit/{id}")
    public String editSeance(@PathVariable("id")int id, @ModelAttribute SeanceEntity seance) {
        System.out.println("movie id : "+seance.getMovie().getId());
        System.out.println("cinema id : "+seance.getCinema().getId());
        System.out.println("id : "+id);
        Optional<SeanceEntity> optionalSeance = seanceRepository.findById(id);
        if (!optionalSeance.isPresent()) {
            return "error/404";
        }
        SeanceEntity existingSeance = optionalSeance.get();

        // Récupérer l'entité Movie à partir de movieId
        Optional<MovieEntity> movie = movieService.getMovieById(seance.getMovie().getId());
        if (movie == null) {
            return "error/404";  // Si le film n'existe pas, retourner une erreur
        }

        // Mise à jour des champs de la séance
        existingSeance.setMovie(movie.get());  // Mettre à jour le film
        existingSeance.setStartTime(seance.getStartTime());
        existingSeance.setDuration(seance.getDuration());
        existingSeance.setCinema(cinemaRepository.findById(seance.getCinema().getId()).orElseThrow(() -> new RuntimeException("Cinema not found")));

        seanceRepository.save(existingSeance);
        return "redirect:/seance/all";
    }

    @GetMapping("/delete/{id}")
    public String deleteSeance(@PathVariable int id) {
        seanceService.deleteSeanceById(id);
        return "redirect:/seance/all";
    }
}
