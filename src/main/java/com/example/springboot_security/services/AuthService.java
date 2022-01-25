package com.example.springboot_security.services;

import com.example.springboot_security.dtos.request.LoginRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.dtos.response.UserResponse;

public interface AuthService {

    UserResponse register(UserRequest userRequest);

    JwtTokenResponse login(LoginRequest loginRequest);


}
