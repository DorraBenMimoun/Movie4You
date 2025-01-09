package org.example.springsecurity.Service;


import jakarta.persistence.EntityManager;
import org.example.springsecurity.Models.RefreshToken;
import org.example.springsecurity.Models.User;
import org.example.springsecurity.Repositories.RefreshTokenRepository;
import org.example.springsecurity.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
public class RefreshTokenService {
    @Value("${dorra.app.jwtRefreshExpirationMs}")
    private long refreshTokenExpirationMs;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EntityManager entityManager;

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }
    public Optional<RefreshToken> findByUserId(Long userId) {return refreshTokenRepository.findByUserId(userId);}


    public RefreshToken createRefreshToken(Long userId) {
        RefreshToken refreshToken = new RefreshToken();

        refreshToken.setUser(userRepository.findById(userId).get());
        refreshToken.setExpiryDate(Instant.now().plusMillis(refreshTokenExpirationMs));
        refreshToken.setToken(UUID.randomUUID().toString());

        refreshToken = refreshTokenRepository.save(refreshToken);
        return refreshToken;
    }
    public boolean deleteByToken(String token) {
        Optional<RefreshToken> refreshToken = refreshTokenRepository.findByToken(token);

        System.out.println("token existe ?: "+refreshToken.isPresent());
        if (refreshToken.isPresent()) {
            refreshTokenRepository.delete(refreshToken.get());
            return true;
        }
        return false;
    }

    public boolean deleteByUserId(Long userId) {
        Optional<RefreshToken> refreshTokenOpt = refreshTokenRepository.findByUserId(userId);

        if (refreshTokenOpt.isPresent()) {
            RefreshToken refreshToken = refreshTokenOpt.get();
            User user = refreshToken.getUser();

            if (user != null) {
                user.setRefreshToken(null); // Dissocie pour éviter les conflits
                userRepository.save(user); // Met à jour l'utilisateur
            }

            refreshTokenRepository.delete(refreshToken); // Supprime le token
            System.out.println("RefreshToken successfully deleted for userId: " + userId);
            return true;
        }

        System.out.println("No RefreshToken found for userId: " + userId);
        return false;
    }



}
