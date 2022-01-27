package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Event;
import com.example.springboot_security.data.repositories.EventRepository;
import com.example.springboot_security.dtos.request.EventRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EventServiceImpl implements EventService{

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private EventRepository eventRepository;

    @Override
    public Event create(EventRequest eventRequest) {

        Event event = modelMapper.map(eventRequest, Event.class);
        return saveEvent(event);
    }

    private Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
}
