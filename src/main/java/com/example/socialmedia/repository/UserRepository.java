package com.example.socialmedia.repository;

import com.example.socialmedia.model.User;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, String> {
    User findByUsername(String username);
    boolean existsByEmail(String username);
    boolean existsByUsername(String username);
}
