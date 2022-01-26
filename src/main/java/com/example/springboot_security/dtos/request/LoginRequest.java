package com.example.springboot_security.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
public class LoginRequest {

    @Email(message = "email cannot be blank")
    private String email;

    @Size(min = 6, max= 20, message= "Invalid password")
    private String password;
}
