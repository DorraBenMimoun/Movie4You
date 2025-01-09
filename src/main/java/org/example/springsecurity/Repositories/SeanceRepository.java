package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.SeanceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SeanceRepository extends JpaRepository<SeanceEntity, Integer> {

    List<SeanceEntity> findByCinemaId(int cinemaId);

}
