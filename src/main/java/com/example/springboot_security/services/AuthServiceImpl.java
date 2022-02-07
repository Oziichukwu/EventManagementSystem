package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Token;
import com.example.springboot_security.data.models.TokenType;
import com.example.springboot_security.data.models.User;
import com.example.springboot_security.data.repositories.TokenRepository;
import com.example.springboot_security.data.repositories.UserRepository;
import com.example.springboot_security.dtos.request.LoginRequest;

import com.example.springboot_security.dtos.request.PasswordRequest;
import com.example.springboot_security.dtos.request.PasswordResetRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.dtos.response.UserResponse;
import com.example.springboot_security.exceptions.AuthException;
import com.example.springboot_security.exceptions.TokenException;
import com.example.springboot_security.security.CustomUserDetailService;
import com.example.springboot_security.security.JwtTokenProvider;
import com.example.springboot_security.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AuthServiceImpl implements AuthService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

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
//        user.setPassword(passwordEncoder.encode(user.getPassword()));
//         user.setVerificationCode(UUID.randomUUID().toString());

        User savedUser = save(user);

        return modelMapper.map(savedUser, UserResponse.class);
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

    @Override
    public void updatePassword(PasswordRequest passwordRequest)throws AuthException {

        String email = passwordRequest.getEmail();
        String oldPassword = passwordRequest.getOldPassword();
        String newPassword = passwordRequest.getPassword();

        User userToUpdatePassword = userRepository.findByEmail(email).orElseThrow(()->
                new AuthException("User with email does not exist "));

        boolean checkForPasswordMatch = passwordEncoder.matches(oldPassword, userToUpdatePassword.getPassword());

        if (!checkForPasswordMatch) {
            throw new AuthException("Passwords do not match");
        }

        userToUpdatePassword.setPassword(passwordEncoder.encode(newPassword));

        save(userToUpdatePassword);
    }

    @Override
    public void resetPassword(PasswordResetRequest passwordResetRequest, String passwordResetToken) throws AuthException, TokenException {

        String email = passwordResetRequest.getEmail();
        String password = passwordResetRequest.getPassword();

        User userToResetPassword = userRepository.findByEmail(email).orElseThrow(()->
                new AuthException("No user with such email exist"));

        Token token = tokenRepository.findByToken(passwordResetToken).orElseThrow(()->
                new TokenException("Token does not exist"));

        if (token.getExpiry().isBefore(LocalDateTime.now())){
            throw new TokenException("This password reset token has expired");
        }

        if (!token.getId().equals(userToResetPassword.getUserId())){
            throw new TokenException("This password token does not exist");

        }
        userToResetPassword.setPassword(passwordEncoder.encode(password));
        save(userToResetPassword);
    }

    @Override
    public Token generatePasswordResetToken(String email) throws AuthException {

        User userToResetPassword = userRepository.findByEmail(email).orElseThrow(()->
                new AuthException("No user with such email"));

        Token token = new Token();
        token.setType(TokenType.PASSWORD_RESET);
        token.setId(userToResetPassword.getUserId());
        token.setToken(UUID.randomUUID().toString());
        token.setExpiry(LocalDateTime.now().plusMinutes(30));

        return tokenRepository.save(token);
    }


    private User internalDatabaseFindUserByEmail(String email) {
        return userRepository.findByEmail(email).orElse(null);
    }

    private User save(User user) {
        return userRepository.save(user);
    }
}
