package com.karold.onlinestore.security.service;

import com.karold.onlinestore.exception.EmailAlreadyInUseException;
import com.karold.onlinestore.exception.TokenRefreshException;
import com.karold.onlinestore.model.Address;
import com.karold.onlinestore.model.User;
import com.karold.onlinestore.repository.AddressRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.security.model.RefreshToken;
import com.karold.onlinestore.security.payload.SignupRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class UserService {

    private UserRepository userRepository;

    private AddressRepository addressRepository;

    private RefreshTokenService refreshTokenService;

    private JWTUtils jwtUtils;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AddressRepository addressRepository, RefreshTokenService refreshTokenService, JWTUtils jwtUtils, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.refreshTokenService = refreshTokenService;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String user){
        return jwtUtils.generateToken(user);
    }

    public String refreshToken(String refreshToken){
        return refreshTokenService.findByToken(refreshToken)
                .map(refreshTokenService::verifyExpiration)
                .map(RefreshToken::getUser)
                .map(user -> {
                    String token = jwtUtils.generateToken(user.getEmail());
                    return token;
                })
                .orElseThrow(()-> new TokenRefreshException("Refresh token not found in database"));
    }

    public String createRefreshToken(Long userId){
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(userId);
        return refreshToken.getToken();
    }

    public boolean register(SignupRequest signupRequest){
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new EmailAlreadyInUseException(signupRequest.getEmail());
        }
        User user = modelMapper.map(signupRequest, User.class);
        Address address = addressRepository.save(signupRequest.getAddress());
        user.createCustomer(address, passwordEncoder);
        userRepository.save(user);
        return true;
    }
}
