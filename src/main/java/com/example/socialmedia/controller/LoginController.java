package com.example.socialmedia.controller;

import com.example.socialmedia.dto.LoginRequest;
import com.example.socialmedia.model.User;
import com.example.socialmedia.service.UserService;
import com.example.socialmedia.service.impl.AuthService;
import com.example.socialmedia.service.impl.InvalidTokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class LoginController {

    private final UserService userService;

    private final AuthService authService;

    private final InvalidTokenService invalidTokenService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    public LoginController(UserService userService, AuthService authService, InvalidTokenService invalidTokenService) {
        this.userService = userService;
        this.authService = authService;
        this.invalidTokenService = invalidTokenService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> createUser(@RequestBody User userToCreate) {
        // Create user
        userService.createUser(userToCreate);

        // Return user details
        return new ResponseEntity<>("User: " + userToCreate.getUsername() + " created successfully.", HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest userToLogin) {
        // Login user + generate token
        String token = authService.verifyUser(userToLogin.username(), userToLogin.password());

        // Return token + success message
        return ResponseEntity.ok("User " + userToLogin.username() + " logged in successfully." + "\nToken: " + token);
    }

    @PostMapping("/logout/{username}")
    public ResponseEntity<?> logout(@PathVariable String username, HttpServletRequest request) {
        // Get token from Authorization header
        String authHeader = request.getHeader("Authorization");
        String token = authHeader.substring(7);

        // Clear security context
        SecurityContextHolder.clearContext();

        // Add token to invalid token list
        invalidTokenService.addToken(token);

        return ResponseEntity.ok("Logout successful");
    }
}
