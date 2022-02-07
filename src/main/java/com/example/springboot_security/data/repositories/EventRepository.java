package com.example.springboot_security.data.repositories;

import com.example.springboot_security.data.models.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {

    boolean existsByName(String name);

}
