package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.LikeEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Integer> {
    boolean existsByUserIdAndReviewId(int userId, int reviewId);
   // void deleteByUserIdAndReviewId(int userId, int reviewId);

    @Modifying
    @Query("DELETE FROM LikeEntity l WHERE l.user.id = :userId AND l.review.id = :reviewId")
    void deleteByUserIdAndReviewId(@Param("userId") int userId, @Param("reviewId") int reviewId);


}
