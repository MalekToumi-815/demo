package com.example.demo.domain.repository;

import com.example.demo.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    // JpaRepository provides save(), findById(), findAll(), deleteById(), etc. out of the box
    User findByEmail(String email);
}