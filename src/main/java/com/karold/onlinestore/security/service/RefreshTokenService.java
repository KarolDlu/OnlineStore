package com.karold.onlinestore.security.service;

import com.karold.onlinestore.exception.TokenRefreshException;
import com.karold.onlinestore.model.User;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.security.model.RefreshToken;
import com.karold.onlinestore.security.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static com.karold.onlinestore.security.config.JWTConstants.REFRESH_EXPIRATION_TIME;

@Service
@Transactional
public class RefreshTokenService {

    private RefreshTokenRepository refreshTokenRepository;

    private UserRepository userRepository;

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository, UserRepository userRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    public RefreshToken createRefreshToken(Long userId) {
        User user = userRepository.findById(userId).get();
        Instant expiryDate = Instant.now().plusMillis(REFRESH_EXPIRATION_TIME);
        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = new RefreshToken(user, token, expiryDate);
        return refreshTokenRepository.save(refreshToken);
    }

    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().compareTo(Instant.now()) < 0){
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token was expired");
        }
        return token;
    }


}
