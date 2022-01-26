package com.example.springboot_security.services;

import com.example.springboot_security.data.models.User;
import com.example.springboot_security.data.repositories.UserRepository;
import com.example.springboot_security.dtos.request.LoginRequest;

import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.dtos.response.UserResponse;
import com.example.springboot_security.exceptions.AuthException;
import com.example.springboot_security.security.CustomUserDetailService;
import com.example.springboot_security.security.JwtTokenProvider;
import com.example.springboot_security.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private CustomUserDetailService customUserDetailService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;


    @Override
    public UserResponse register(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())){
            throw new AuthException("Email Already Exist");
        }

        User user = modelMapper.map(userRequest, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
         user.setVerificationCode(UUID.randomUUID().toString());

        User savedUser = save(user);

        return modelMapper.map(savedUser, UserResponse.class);
    }

    private User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public JwtTokenResponse login(LoginRequest loginRequest) {

        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserPrincipal userDetails = (UserPrincipal) customUserDetailService.loadUserByUsername(loginRequest.getEmail());
        final String token = jwtTokenProvider.generateToken(userDetails);
        User user = internalDatabaseFindUserByEmail(loginRequest.getEmail());
        return new JwtTokenResponse(token , user.getEmail());
    }

    private User internalDatabaseFindUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }
}
