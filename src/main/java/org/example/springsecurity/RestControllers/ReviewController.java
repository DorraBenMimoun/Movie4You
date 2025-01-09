package org.example.springsecurity.RestControllers;

import org.example.springsecurity.DTO.ReviewDTO;
import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    // Ajouter une critique
    @PostMapping("/")
    public ResponseEntity<ReviewEntity> addReview(@RequestBody ReviewDTO reviewDTO) {
        long userId = reviewDTO.getUserId();
        int movieId = reviewDTO.getMovieId();
        String content = reviewDTO.getContent();

        ReviewEntity review = reviewService.addReview(userId, movieId, content);
        return ResponseEntity.ok(review);
    }

    // Récupérer toutes les critiques d'un utilisateur
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewEntity>> getAllReviewsForUser(@PathVariable int userId) {
        List<ReviewEntity> reviews = reviewService.getAllReviewsByUser(userId);
        return ResponseEntity.ok(reviews);
    }

    // Supprimer une critique par son ID
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> removeReview(@PathVariable int reviewId) {
        reviewService.removeReview(reviewId);
        return ResponseEntity.noContent().build();
    }

    // Mettre à jour une critique
    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewEntity> updateReview(@PathVariable int reviewId, @RequestBody ReviewDTO reviewDTO) {
        ReviewEntity updatedReview = reviewService.updateReview(reviewId, reviewDTO.getContent());
        return ResponseEntity.ok(updatedReview);
    }

    // Récupérer une critique par son ID
    @GetMapping("/{reviewId}")
    public ResponseEntity<Optional<ReviewEntity>> getReviewById(@PathVariable int reviewId) {
        Optional<ReviewEntity> review = reviewService.getReviewById(reviewId);
        return ResponseEntity.ok(review);
    }

    // Récupérer toutes les critiques
    @GetMapping("/")
    public ResponseEntity<List<ReviewEntity>> getAllReviews() {
        List<ReviewEntity> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }



}
