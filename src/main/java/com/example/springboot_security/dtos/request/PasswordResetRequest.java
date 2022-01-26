package com.example.springboot_security.dtos.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequest {

    @Email(message= "email is required")
    private String email;

    @NotBlank(message= "password cannot be blank")
    private String password;
}
