package com.example.springboot_security.data.repositories;

import com.example.springboot_security.data.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail (String email);

    Optional<User> findByVerificationCode(String verificationToken);

}
