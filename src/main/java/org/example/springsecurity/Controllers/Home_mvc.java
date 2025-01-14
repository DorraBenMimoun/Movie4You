package org.example.springsecurity.Controllers;

import jakarta.servlet.http.HttpSession;
import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller

public class Home_mvc {

    @Autowired
    private MovieService movieService;

    // Affichage de la page d'accueil
    @GetMapping("/")
    public String homePage(Model model, HttpSession session) {
        // Envoie une liste de tous les films pour l'affichage
        List<MovieEntity> movies = movieService.getAllMovies();
        model.addAttribute("movies", movies);
        if (session.getAttribute("roles") == null) {
            session.setAttribute("roles", new ArrayList<>());
        }


        String username = (String) session.getAttribute("username");
        model.addAttribute("username", username);
        return "home/home";
    }

    // Rechercher un film par nom
    @GetMapping("/search")
    public String searchMovies(@RequestParam("query") String query, Model model) {
        // Recherche des films correspondant à la requête
        List<MovieEntity> searchResults = movieService.searchMoviesByName(query);
        model.addAttribute("movies", searchResults);
        model.addAttribute("query", query);  // Pour afficher la requête dans le champ de recherche
        return "home/home";
    }
}
