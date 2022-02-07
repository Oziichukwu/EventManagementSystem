package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Event;
import com.example.springboot_security.dtos.request.EventRequest;
import com.example.springboot_security.dtos.response.EventResponse;

public interface EventService {

    EventResponse create(EventRequest eventRequest);
}
