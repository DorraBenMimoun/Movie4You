package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.MovieEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MovieRepository extends JpaRepository<MovieEntity, Integer> {
    List<MovieEntity> findByNameContaining(String name);
    MovieEntity findBySlug(String slug);
    List<MovieEntity> findByGenre(String genre);
    Optional<MovieEntity> findById(Integer id);

    List<MovieEntity> findByNameContainingIgnoreCase(String name);
    List<MovieEntity> findByName(String name);
}

