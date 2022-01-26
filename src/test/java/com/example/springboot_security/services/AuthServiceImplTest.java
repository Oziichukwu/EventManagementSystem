package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Role;
import com.example.springboot_security.data.models.RoleName;
import com.example.springboot_security.data.models.User;
import com.example.springboot_security.data.repositories.UserRepository;
import com.example.springboot_security.dtos.request.LoginRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.dtos.response.JwtTokenResponse;
import com.example.springboot_security.exceptions.AuthException;
import com.example.springboot_security.security.CustomUserDetailService;
import com.example.springboot_security.security.JwtTokenProvider;
import com.example.springboot_security.security.UserPrincipal;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.TestingAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class AuthServiceImplTest {


    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private CustomUserDetailService customUserDetailService;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    private User mockedUser;

    @InjectMocks
    private AuthServiceImpl authService;

    @Mock
    private ModelMapper modelMapper;

    @BeforeEach
    void setUp() {
        mockedUser = new User();
        mockedUser.setUserId(1L);
        mockedUser.setFirstName("Goodnews");
        mockedUser.setLastName("Uchechukwu");
        mockedUser.setEmail("goodnews@gmail.com");
        mockedUser.setPassword("123Ugc@@@");
        mockedUser.setPhoneNumber("08100841169");

        Role role = new Role(RoleName.ROLE_USER);
        mockedUser.getRoles().add(role);
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
    }

    @Test
    void userCanRegister() throws AuthException{

        UserRequest userRequest = new UserRequest();
        //userRequest.setEmail("goodnews@gmail.com");

        //Given
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(modelMapper.map(userRequest, User.class)).thenReturn(mockedUser);
        when(userRepository.save(any(User.class))).thenReturn(mockedUser);
        //doNothing().when(emailService).sendUserVerificationEmail(any());

        //when
        authService.register(userRequest);

        //Assert
        verify(userRepository, times(1)).existsByEmail(mockedUser.getEmail());
        verify(userRepository, times(1)).save(mockedUser);
    }

    @Test
    void whenLoginMethodIsCalled_FindUserByEmailIsCalled(){

        //Given
        LoginRequest loginRequest = new LoginRequest("goodnews@gmail.com", "123Ugc@@@");
        when(userRepository.findByEmail("goodnews@gmail.com")).thenReturn(Optional.of(mockedUser));

        TestingAuthenticationToken testingAuthenticationToken = new TestingAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        );

        testingAuthenticationToken.setAuthenticated(true);
        testingAuthenticationToken.setDetails(loginRequest);

        //When
        when(authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ))).thenReturn(testingAuthenticationToken);
        SecurityContextHolder.getContext().setAuthentication(testingAuthenticationToken);

        UserPrincipal userPrincipal = modelMapper.map(mockedUser, UserPrincipal.class);

        when(userRepository.findByEmail(anyString())).thenReturn(Optional.of(mockedUser));
        UserPrincipal foundUser = (UserPrincipal) customUserDetailService.loadUserByUsername(loginRequest.getEmail());
        String actualToken = jwtTokenProvider.generateToken(foundUser);

        when(customUserDetailService.loadUserByUsername(anyString())).thenReturn(foundUser);
        when(jwtTokenProvider.generateToken(any(UserPrincipal.class))).thenReturn(actualToken);

        //Assert

        JwtTokenResponse jwtTokenResponse = authService.login(loginRequest);
        verify(customUserDetailService, times(2)).loadUserByUsername(loginRequest.getEmail());
        verify(jwtTokenProvider, times(2)).generateToken(userPrincipal);
        verify(userRepository, times(1)).findByEmail(loginRequest.getEmail());

        assertNotNull(jwtTokenResponse);
        assertEquals(jwtTokenResponse.getJwtToken(), actualToken);
        assertEquals(jwtTokenResponse.getEmail(), loginRequest.getEmail());

    }


}