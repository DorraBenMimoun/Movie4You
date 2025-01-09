package org.example.springsecurity.Repositories;


import org.example.springsecurity.Models.User;
import org.hibernate.validator.constraints.NotBlank;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
  Optional <User> findByUsername(String username);
  Optional <User> findByEmail(String email);
  Optional <User> findByUsernameAndPassword(String username, String password);
  Optional <User> findByEmailAndPassword(String email, String password);
  Optional <User> findById(int id);

    boolean existsByUsername(String username);

  boolean existsByEmail( String email);
}
