package com.example.springboot_security.dtos.request;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Getter
@Setter
public class UserRequest {

    @NotBlank(message = "firstname cannot be blank")
    private String firstName;

    @NotBlank(message = "lastname cannot be blank")
    private String lastName;

    @Email(message = "Enter a valid email")
    private String email;

    @Size(min = 6, max=20, message = "Invalid password")
    @NotBlank(message = "Password cannot be blank")
    private String password;

    private String phoneNumber;
}
