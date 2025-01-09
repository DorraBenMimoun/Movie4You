package org.example.springsecurity.Repositories;


import org.example.springsecurity.Models.AdminEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface AdminRepository extends JpaRepository<AdminEntity, Integer> {

    AdminEntity findByUsername(String username);

    AdminEntity findByUsernameAndPassword(String username, String password);

    List<AdminEntity> findAll();
}
