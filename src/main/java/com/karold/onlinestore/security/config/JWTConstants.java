package com.karold.onlinestore.security.config;

public class JWTConstants {
    public static final long EXPIRATION_TIME = 30000;
    public static final long REFRESH_EXPIRATION_TIME = 60000;
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
}
