package org.example.springsecurity.Repositories;

import org.example.springsecurity.Models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String token);
    Optional<RefreshToken> findByUserId(Long userId);
    //void deleteByUserId(Long userId);
    //void deleteByRefreshToken(String refreshToken);
    //void deleteByToken(String token);
    void removeByToken(String token);

}
