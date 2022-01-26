package com.example.springboot_security.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
public class JwtTokenResponse {

    @NotBlank(message = "Token cannot be null!")
    private String jwtToken;

    @Email(message = "Email must be valid")
    private String email;
}
