package com.example.Web_application.repository;
//rosan make anything

import com.example.Web_application.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}