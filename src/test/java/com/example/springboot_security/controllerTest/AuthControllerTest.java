package com.example.springboot_security.controllerTest;

import com.example.springboot_security.data.models.Token;
import com.example.springboot_security.data.repositories.UserRepository;
import com.example.springboot_security.dtos.request.LoginRequest;
import com.example.springboot_security.dtos.request.UserRequest;
import com.example.springboot_security.services.AuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private String registerJsonObject;

    @MockBean
    private AuthService authService;


    @MockBean
    private UserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setUp() throws JsonProcessingException {
        UserRequest userDto = new UserRequest();
        userDto.setFirstName("uche");
        userDto.setLastName("vince");
        userDto.setEmail("vince@gmail.com");
        userDto.setPassword("password123");
        registerJsonObject = objectMapper.writeValueAsString(userDto);
    }

        @AfterEach
        void tearDown() {
            userRepository.deleteAll();
        }

    @Test
    void WhenUserRegistersWithValidInputReturn201_Status() throws Exception {

        mockMvc.perform(post("/api/v1/goodyTask/auth/register")
                        .contentType("application/json")
                        .content(registerJsonObject))
                .andExpect(status().is(201))
                .andDo(print());
    }

    @Test
    void whenUserLoginWithValidInput_return200() throws Exception {

        LoginRequest loginRequest = new LoginRequest("vince@gmail.com", "password123");

        MvcResult mvcResult = mockMvc.perform(post("/api/v1/goodyTask/auth/login")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().is(200))
                .andDo(print())
                .andReturn();

        int expectedStatus = 200;
        int actualStatus = mvcResult.getResponse().getStatus();
        assertThat(expectedStatus).isEqualTo(actualStatus);
    }

//    @Test
//    void whenUserForgetPassword_thenReturn201() throws Exception {
//        when(authService.generatePasswordResetToken(anyString())).thenReturn(new Token());
//        MvcResult mvcResult = mockMvc.perform(get("/api/v1/goodyTask/auth/password/reset/vince@gmail.com")
//                        .contentType("application/json"))
//                .andDo(print())
//                .andExpect(status().isCreated()).andReturn();
//
//        //Then
//        int expectedStatus = 201;
//        int actualStatus = mvcResult.getResponse().getStatus();
//        assertThat(expectedStatus).isEqualTo(actualStatus);
//    }
}
