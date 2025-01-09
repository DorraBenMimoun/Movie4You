package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.CinemaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CinemaRepository extends JpaRepository<CinemaEntity, Integer> {


    List<CinemaEntity> findAll();

    List<CinemaEntity> findByNameContaining(String name);

    List<CinemaEntity> findByLocation(String location);




}

