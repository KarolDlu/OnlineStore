package com.karold.onlinestore.security.payload;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AuthResponse {

    private String accessToken;

    private String typeToken = "Bearer";

    public AuthResponse(String accessToken) {
        this.accessToken = accessToken;
    }
}
