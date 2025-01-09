package org.example.springsecurity.Controllers;


import jakarta.servlet.http.HttpSession;
import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Models.SeanceEntity;
import org.example.springsecurity.Repositories.ReviewRepository;
import org.example.springsecurity.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/reviews")
public class Review_mvc {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewRepository reviewRepository;
    @GetMapping("/all")
    public String getAllReviews(Model model) {
        List<ReviewEntity> reviews = reviewRepository.findAll();
        model.addAttribute("reviews", reviews);
        return "reviews/list";
    }

    @GetMapping("/add")
    public String showAddReviewForm(Model model) {

        return "reviews/addReview";
    }
    @PostMapping("/add")
    public String addReview(
            @RequestParam int movieId,
            @RequestParam String content,
            HttpSession session,
            Model model) {
        Long userId = (Long) session.getAttribute("userId");

        if(userId == null) {
            return "redirect:/users/login";
        }
        ReviewEntity review = reviewService.addReview(userId, movieId, content);
        model.addAttribute("review", review);
        model.addAttribute("userId", userId);
       // return "reviews/reviewAdded";
        return "redirect:/movie/"+movieId;

    }

    @GetMapping("/user/{userId}")
    public String getAllReviewsForUser(@PathVariable int userId, Model model) {
        try {
            List<ReviewEntity> reviews = reviewService.getAllReviewsByUser(userId);
            if (reviews.isEmpty()) {
                model.addAttribute("message", "Aucune critique trouvée pour cet utilisateur.");
            }
            model.addAttribute("reviews", reviews);
            return "reviews/userReviews";
        } catch (RuntimeException e) {
            e.printStackTrace(); // Pour identifier l'erreur
            model.addAttribute("error", "Une erreur inattendue est survenue lors de la récupération des critiques.");
            return "error"; // Page d'erreur personnalisée
        }
    }



    @GetMapping("/delete/{reviewId}")
    public String removeReview(@PathVariable int reviewId, Model model) {
        reviewService.removeReview(reviewId);
        model.addAttribute("message", "Critique supprimée avec succès");
        return "reviews/reviewDeleted";
    }
}