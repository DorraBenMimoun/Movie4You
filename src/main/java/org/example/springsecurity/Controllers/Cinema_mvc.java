package org.example.springsecurity.Controllers;


import org.example.springsecurity.DTO.CinemaDTO;
import org.example.springsecurity.Models.CinemaEntity;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Repositories.CinemaRepository;
import org.example.springsecurity.Repositories.MovieRepository;
import org.example.springsecurity.Service.CinemaService;
import org.example.springsecurity.Service.MovieService;
import org.example.springsecurity.Service.SeanceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/cinema")
public class Cinema_mvc {

    @Autowired
    private CinemaService cinemaService;
    @Autowired
    private CinemaRepository cinemaRepository;

    @Autowired
    private MovieService movieService;
    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private SeanceService seanceService;

    @GetMapping("/all")
    public String getAllCinemas(Model model) {
        List<CinemaEntity> cinemas = cinemaService.getAllCinemas();
        model.addAttribute("cinemas", cinemas);
        return "cinema/cinemas";
    }



    @GetMapping("/details/{id}")
    public String getCinemaDetails(@PathVariable int id, Model model) {
        // Récupérer les informations du cinéma
        CinemaEntity cinema = cinemaService.getAllCinemas().stream()
                .filter(c -> c.getId() == id)
                .findFirst()
                .orElse(null);
        model.addAttribute("cinema", cinema);

        if (cinema == null) {
            model.addAttribute("error", "Cinéma non trouvé.");
            return "error"; // Vue pour gérer les erreurs
        }

        // Récupérer les séances associées au cinéma
        List<SeanceEntity> seances = seanceService.getSeancesByCinemaId(id);

        // Extraire les films uniques des séances
        List<MovieEntity> listFilm = seances.stream()
                .map(SeanceEntity::getMovie) // Récupère le film de chaque séance
                .distinct() // Supprime les doublons
                .collect(Collectors.toList());

        // Ajouter la liste des films au modèle
        model.addAttribute("movies", listFilm);

        return "cinema/cinema-details"; // Vue correspondante
    }


    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("cinemaDTO", new CinemaDTO());
        return "cinema/create-cinema";
    }

    @PostMapping("/create")
    public String createCinema(@ModelAttribute CinemaDTO cinemaDTO) {
        CinemaEntity cinema = new CinemaEntity();
        cinema.setName(cinemaDTO.getName());
        cinema.setLocation(cinemaDTO.getLocation());
        cinema.setCapacity(cinemaDTO.getCapacity());
        cinema.setContactInfo(cinemaDTO.getContactInfo());
        cinemaService.addCinema(cinema);
        return "redirect:/cinema/all";
    }


    @GetMapping("/delete/{id}")
    public String deleteCinema(@PathVariable  Integer id) {
        cinemaService.deleteCinemaById(id);
        return "redirect:/cinema/all";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable("id") int id, Model model) {
        Optional<CinemaEntity> cinemaOptional = cinemaRepository.findById(id);
        if (!cinemaOptional.isPresent()) {
            return "error/404";
        }
        model.addAttribute("cinema", cinemaOptional.get());
        return "cinema/edit";
    }

    @PostMapping("/edit/{id}")
    public String editCinema(@PathVariable("id") int id, @ModelAttribute CinemaEntity cinemaInput, Model model) {
        Optional<CinemaEntity> optionalCinema = cinemaRepository.findById(id);
        if (!optionalCinema.isPresent()) {
            model.addAttribute("error", "Cinema not found!");
            return "error/404";
        }

        CinemaEntity existingCinema = optionalCinema.get();
        updateCinemaFields(existingCinema, cinemaInput);
        cinemaRepository.save(existingCinema);

        return "redirect:/cinema/all";
    }

    private void updateCinemaFields(CinemaEntity existingCinema, CinemaEntity cinemaInput) {
        if (cinemaInput.getName() != null && !cinemaInput.getName().trim().isEmpty())
            existingCinema.setName(cinemaInput.getName());
        if (cinemaInput.getLocation() != null && !cinemaInput.getLocation().trim().isEmpty())
            existingCinema.setLocation(cinemaInput.getLocation());

        if (cinemaInput.getContactInfo() != null && !cinemaInput.getContactInfo().trim().isEmpty())
            existingCinema.setContactInfo(cinemaInput.getContactInfo());
    }
}

