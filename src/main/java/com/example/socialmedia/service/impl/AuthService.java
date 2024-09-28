package com.example.socialmedia.service.impl;

import com.example.socialmedia.exception.UnauthorisedAccessException;
import com.example.socialmedia.exception.UserNotAuthException;
import com.example.socialmedia.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JWTService jwtService;

    // Method to verify user and returns token
    public String verifyUser(String username, String password) {
        // Authenticate user with given username and password
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password)
        );

        // Throw exception if user is not authenticated
        if (!authentication.isAuthenticated()) {
            throw new UserNotAuthException(username);
        }

        // Return token to authenticated user (Valid for 15 minutes)
        return jwtService.generateToken(username);
    }

}
