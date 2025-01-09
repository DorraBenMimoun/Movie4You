package org.example.springsecurity.Service;

import jakarta.transaction.Transactional;

import org.example.springsecurity.Models.LikeEntity;
import org.example.springsecurity.Models.ReviewEntity;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.LikeRepository;
import org.example.springsecurity.Repositories.ReviewRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final ReviewRepository reviewRepository;

    public LikeService(LikeRepository likeRepository, UserRepository userRepository, ReviewRepository reviewRepository) {
        this.likeRepository = likeRepository;
        this.userRepository = userRepository;
        this.reviewRepository = reviewRepository;
    }

    public LikeEntity likeReview(int userId, int reviewId) {
        if (likeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new IllegalStateException("User has already liked this review");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        ReviewEntity review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("Review not found"));

        LikeEntity like = new LikeEntity();
        like.setUser(user);
        like.setReview(review);

        return likeRepository.save(like);
    }


    @Transactional
    public void unlikeReview(int userId, int reviewId) {
        if (!likeRepository.existsByUserIdAndReviewId(userId, reviewId)) {
            throw new IllegalStateException("User has not liked this review");
        }
        likeRepository.deleteByUserIdAndReviewId(userId, reviewId);
    }
}
