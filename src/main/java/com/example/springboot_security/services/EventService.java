package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Event;
import com.example.springboot_security.dtos.request.EventRequest;

public interface EventService {

    Event create(EventRequest eventRequest);
}
