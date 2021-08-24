package com.karold.onlinestore.security.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.karold.onlinestore.model.User;
import com.karold.onlinestore.security.config.JWTUtils;
import com.karold.onlinestore.security.model.CustomUserDetails;
import com.karold.onlinestore.security.payload.AuthResponse;
import com.karold.onlinestore.security.payload.LoginRequest;
import com.karold.onlinestore.security.payload.SignupRequest;
import com.karold.onlinestore.security.service.UserService;
import lombok.Getter;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import static com.karold.onlinestore.security.config.JWTConstants.HEADER_STRING;
import static com.karold.onlinestore.security.config.JWTConstants.TOKEN_PREFIX;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthenticationManager authenticationManager;

    private UserService userService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String user = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
        String token = userService.login(user);
        return ResponseEntity.ok(new AuthResponse(token));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.register(signupRequest)) {
            return new ResponseEntity<>("Success! User has registered", HttpStatus.CREATED);
        }
        return ResponseEntity.ok("User registration field");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<AuthResponse> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(new AuthResponse(userService.refreshToken(token)));
    }

    @GetMapping("/checkSecurity")
    public ResponseEntity<String> securedEndPoint() {
        return ResponseEntity.ok("Secured endpoint!");
    }
}
