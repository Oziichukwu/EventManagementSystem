package com.example.springboot_security.controller;


import com.example.springboot_security.dtos.request.LoginRequest;
import com.example.springboot_security.dtos.request.PasswordRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.ApiResponse;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.dtos.response.UserResponse;
import com.example.springboot_security.exceptions.AuthException;
import com.example.springboot_security.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RequestMapping("/api/v1/goodyTask/auth")
@RestController
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<?> register (@Valid @RequestBody UserRequest userRequest){

        try {
            UserResponse userResponse = authService.register(userRequest);
            return new ResponseEntity<>(userResponse, HttpStatus.CREATED);
        }catch (AuthException e){
            return new ResponseEntity<>(e.getLocalizedMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequest loginRequest){
        JwtTokenResponse authenticationDetails = authService.login(loginRequest);
        return new ResponseEntity<>(authenticationDetails, HttpStatus.OK);
    }

    @PostMapping("/password/update")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody PasswordRequest passwordRequest){

        try{
            authService.updatePassword(passwordRequest);
            return new ResponseEntity<>(new ApiResponse(true, "User Password is Successfully Updated"), HttpStatus.OK);
        }catch(AuthException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
