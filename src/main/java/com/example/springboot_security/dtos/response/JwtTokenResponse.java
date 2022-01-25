package com.example.springboot_security.dtos.response;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

public class JwtTokenResponse {

    @NotBlank(message = "Token cannot be null!")
    private String jwtToken;

    @Email(message = "Email must be valid")
    private String email;
}
