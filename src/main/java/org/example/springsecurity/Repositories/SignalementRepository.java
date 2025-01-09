package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.SignalementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SignalementRepository extends JpaRepository<SignalementEntity, Integer> {
    List<SignalementEntity> findByStatus(Boolean status);
}
