package com.example.springboot_security.exceptions;

import com.example.springboot_security.dtos.response.EventResponse;

import javax.validation.constraints.NotBlank;

public class EventAlreadyExistException extends GoodyyTaskException {

    public EventAlreadyExistException(String message) {
        super(message);
    }
}
