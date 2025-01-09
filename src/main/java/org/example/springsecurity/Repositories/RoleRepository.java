package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.ERole;
import org.example.springsecurity.Models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
   Optional<Role> findByName(ERole name);

}
