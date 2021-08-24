package com.karold.onlinestore.security.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.karold.onlinestore.exception.EmailAlreadyInUseException;
import com.karold.onlinestore.model.Address;
import com.karold.onlinestore.model.User;
import com.karold.onlinestore.repository.AddressRepository;
import com.karold.onlinestore.repository.UserRepository;
import com.karold.onlinestore.security.config.JWTUtils;
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

    private JWTUtils jwtUtils;

    private ModelMapper modelMapper;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, AddressRepository addressRepository, JWTUtils jwtUtils, ModelMapper modelMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.jwtUtils = jwtUtils;
        this.modelMapper = modelMapper;
        this.passwordEncoder = passwordEncoder;
    }

    public String login(String user){
        return jwtUtils.generateToken(user);
    }

    public String refreshToken(String token){
        String user = jwtUtils.parseToken(token);
        return jwtUtils.generateRefreshToken(user);
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
