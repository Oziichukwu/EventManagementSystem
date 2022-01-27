package com.example.springboot_security.dtos.request;

import com.example.springboot_security.data.models.EventType;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
public class EventRequest {

    @NotBlank(message = "event name cannot be blank")
    private String name;

    private String description;

    @NotBlank(message = "event type can not be blank")
    @Enumerated(EnumType.STRING)
    private EventType eventType;
}
