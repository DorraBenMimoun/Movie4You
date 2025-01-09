package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends JpaRepository<ReviewEntity,Integer> {
    List<ReviewEntity> findByUserId(int userId);
    Optional<ReviewEntity> findById(int id);
}
