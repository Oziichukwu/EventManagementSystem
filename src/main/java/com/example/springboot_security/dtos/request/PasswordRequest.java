package com.example.springboot_security.dtos.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordRequest {

    @Email(message= "Email must be valid")
    private String email;

    @Size(min = 6, max=20, message="Password cannot be blank")
    private String oldPassword;

    @NotBlank(message= "Password cannot be blank")
    private String Password;
}
