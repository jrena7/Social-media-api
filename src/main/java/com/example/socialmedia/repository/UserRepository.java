package com.example.socialmedia.repository;

import com.example.socialmedia.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    boolean existsByEmail(String username);
    boolean existsByUsername(String username);
}
