package com.karold.onlinestore.security.controller;

import com.karold.onlinestore.security.model.CustomUserDetails;
import com.karold.onlinestore.security.model.RefreshToken;
import com.karold.onlinestore.security.payload.AuthResponse;
import com.karold.onlinestore.security.payload.LoginRequest;
import com.karold.onlinestore.security.payload.SignupRequest;
import com.karold.onlinestore.security.service.RefreshTokenService;
import com.karold.onlinestore.security.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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

        CustomUserDetails customUserDetails = ((CustomUserDetails) authentication.getPrincipal());
        String token = userService.login(customUserDetails.getEmail());
        String refreshToken= userService.createRefreshToken(customUserDetails.getId());
        return ResponseEntity.ok(new AuthResponse(token, refreshToken));
    }

    @PostMapping("/signup")
    public ResponseEntity<String> register(@Valid @RequestBody SignupRequest signupRequest) {
        if (userService.register(signupRequest)) {
            return new ResponseEntity<>("Success! User has registered", HttpStatus.CREATED);
        }
        return ResponseEntity.ok("User registration field");
    }

    @PostMapping("/refreshToken")
    public ResponseEntity<String> refreshToken(@RequestParam String token) {
        return ResponseEntity.ok(userService.refreshToken(token));
    }

    @GetMapping("/checkSecurity")
    public ResponseEntity<String> securedEndPoint() {
        return ResponseEntity.ok("Secured endpoint!");
    }
}
