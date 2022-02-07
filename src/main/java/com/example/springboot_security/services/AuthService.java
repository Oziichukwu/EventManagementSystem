package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Token;
import com.example.springboot_security.dtos.request.LoginRequest;
import com.example.springboot_security.dtos.request.PasswordRequest;
import com.example.springboot_security.dtos.request.PasswordResetRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.dtos.response.UserResponse;
import com.example.springboot_security.exceptions.AuthException;
import com.example.springboot_security.exceptions.TokenException;

import java.io.UnsupportedEncodingException;

public interface AuthService {

    UserResponse register(UserRequest userRequest);

    JwtTokenResponse login(LoginRequest loginRequest);

    void updatePassword(PasswordRequest passwordRequest)throws AuthException, TokenException;

    void resetPassword(PasswordResetRequest passwordResetRequest, String passwordResetToken)throws AuthException, TokenException;

    Token generatePasswordResetToken(String email)throws AuthException;

}
