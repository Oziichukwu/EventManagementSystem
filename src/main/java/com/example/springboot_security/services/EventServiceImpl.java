package com.example.springboot_security.services;

import com.example.springboot_security.data.models.Event;
import com.example.springboot_security.data.repositories.EventRepository;
import com.example.springboot_security.dtos.request.EventRequest;
import com.example.springboot_security.dtos.response.EventResponse;
import com.example.springboot_security.exceptions.EventAlreadyExistException;
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
    public EventResponse create(EventRequest eventRequest) {

        if (eventRepository.existsByName(eventRequest.getName())){
            throw new EventAlreadyExistException("Event with name " + eventRequest.getName() + "already exist");
        }
        Event event = modelMapper.map(eventRequest, Event.class);
         Event savedEvent =saveEvent(event);

         return modelMapper.map(savedEvent, EventResponse.class);
    }

    private Event saveEvent(Event event) {
        return eventRepository.save(event);
    }
}
