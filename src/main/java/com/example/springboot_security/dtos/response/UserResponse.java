package com.example.springboot_security.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
public class UserResponse {

    private String firstName;

    private String lastName;

    private String email;

    private String verificationCode;

    @JsonFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    private LocalDateTime datePublished;


    @JsonFormat(pattern = "yyyy-MM-ss HH:mm:ss")
    @UpdateTimestamp
    private LocalDateTime updatedDate;

}
