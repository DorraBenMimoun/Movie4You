package org.example.springsecurity.Service;


import org.example.springsecurity.Models.MovieEntity;
import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.MovieRepository;
import org.example.springsecurity.Repositories.ReviewRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private MovieRepository movieRepository;
    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewEntity addReview(Long userId, int movieId, String content) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        MovieEntity movie = movieRepository.findById(movieId).orElseThrow(() -> new RuntimeException("Movie not found"));

        ReviewEntity review = new ReviewEntity();
        review.setUser(user);
        review.setMovie(movie);
        review.setContent(content);
        review.setReviewDate(LocalDateTime.now());

        return reviewRepository.save(review);
    }

    public List<ReviewEntity> getAllReviewsByUser(int userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<ReviewEntity> getAllReviewsByUser2(int userId) {
        List<ReviewEntity> reviews = reviewRepository.findByUserId(userId);
        System.out.println("Nombre de critiques trouvées : " + reviews.size());
        return reviews;
    }


    public void removeReview(int reviewId) {
        Optional<ReviewEntity> review = reviewRepository.findById(reviewId);
        review.ifPresent(reviewRepository::delete);
    }


    public List<ReviewEntity> getAllReviews() {
        return reviewRepository.findAll();
    }

    public Optional<ReviewEntity> getReviewById(int id) {
        return reviewRepository.findById(id);
    }

    public ReviewEntity updateReview(int id, String content) {
        ReviewEntity review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        review.setContent(content);
        reviewRepository.save(review);
        return review;
    }
}
