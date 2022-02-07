package com.example.springboot_security.dtos.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiResponse {

    private boolean isSuccessful;

    private String message;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime localDateTime;


    public ApiResponse(boolean isSuccessful, String message) {
        this.isSuccessful = isSuccessful;
        this.message = message;
        localDateTime = localDateTime.now();
    }


}
