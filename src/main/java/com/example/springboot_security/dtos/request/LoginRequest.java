package com.example.springboot_security.dtos.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class LoginRequest {

    @Email(message = "email cannot be blank")
    private String email;

    @Size(min = 6, max= 20, message= "Invalid password")
    private String password;
}
