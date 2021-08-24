package com.karold.onlinestore.security.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

import static com.auth0.jwt.algorithms.Algorithm.HMAC512;
import static com.karold.onlinestore.security.config.JWTConstants.*;

@Component
public class JWTUtils {

    @Value("${karold.online-store.jwtSecret}")
    private String secret;

    public String generateToken(String user) {
        return JWT.create()
                .withSubject(user)
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));
    }

    public String generateRefreshToken(String user) {
        return JWT.create()
                .withSubject(user)
                .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                .sign(HMAC512(secret.getBytes()));
    }

    public String validateAndParseToken(String token) {
        return JWT.require(Algorithm.HMAC512(secret.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX, ""))
                .getSubject();
    }

    public String parseToken(String token){
        return JWT.decode(token.replace(TOKEN_PREFIX, "")).getSubject();
    }

}
