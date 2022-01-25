package com.example.springboot_security.security;


import com.example.springboot_security.data.models.User;
import com.example.springboot_security.data.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import static java.lang.String.format;

@Component
public class CustomUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository.findByEmail(email).orElseThrow(()->
           new UsernameNotFoundException(format("User not found with email %s", email)));
        return UserPrincipal.create(user);
    }
}
