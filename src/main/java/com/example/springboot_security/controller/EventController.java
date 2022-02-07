package com.example.springboot_security.controller;


import com.example.springboot_security.data.models.Event;
import com.example.springboot_security.dtos.request.EventRequest;
import com.example.springboot_security.dtos.response.ApiResponse;
import com.example.springboot_security.dtos.response.EventResponse;
import com.example.springboot_security.exceptions.EventAlreadyExistException;
import com.example.springboot_security.services.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/goodyTask")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping("/create_Event")
    public ResponseEntity<?> createEvent(@RequestBody EventRequest eventRequest){

        try{
            EventResponse event = eventService.create(eventRequest);
            return new ResponseEntity<>(event, HttpStatus.CREATED);
        }catch(EventAlreadyExistException e){
            return new ResponseEntity<>(new ApiResponse(false, e.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }
}
