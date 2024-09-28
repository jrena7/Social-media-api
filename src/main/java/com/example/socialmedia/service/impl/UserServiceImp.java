package com.example.socialmedia.service.impl;

import com.example.socialmedia.exception.UserNotFoundException;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class UserServiceImp implements UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImp(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void createUser(User userToCreate) {
        // User information to register
        validateDetails(userToCreate);

        if (userRepository.existsByEmail(userToCreate.getEmail()) || userRepository.existsByUsername(userToCreate.getUsername())) {
            // User already exists
            throw new IllegalArgumentException("Existing user with Username/ Email.");
        }

        userToCreate.setPassword(passwordEncoder.encode(userToCreate.getPassword()));
        userRepository.save(userToCreate);
    }

    private void validateDetails (User userToCreate) {
        if (!isValidEmail(userToCreate.getEmail())) {
            throw new IllegalArgumentException("Invalid email format");
        }

        if (!isValidUsername(userToCreate.getUsername())) {
            throw new IllegalArgumentException("Invalid username format");
        }

        if (!isValidPassword(userToCreate.getPassword())) {
            throw new IllegalArgumentException("Invalid password format");
        }
    }

    // Email, username and password validation
    private static Boolean isValidEmail(String email) {
        // Only allows emails with a-z, A-Z, 0-9, and special characters: _+&*-
        // Followed by an @ symbol
        // Followed by "." and min 2 i.e .ie, .eu etc
        String emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
        return email.matches(emailRegex);
    }

    private static Boolean isValidUsername(String username) {
        // Only allows usernames with a-z, A-Z, 0-9 or underscore
        // Min 3 and max 20 characters
        return username.matches("^[a-zA-Z0-9_]{3,20}$");
    }

    private static Boolean isValidPassword(String password) {
        // Only allows passwords with a-z, A-Z, 0-9 or special characters
        // Min 8 and max 20 characters
        return password.matches("^[a-zA-Z0-9_!@#$%^&*()]{8,20}$");
    }


    @Override
    // Method to find a user
    public User findUserByName(String username) {
        // Find user by username
        User user = userRepository.findByUsername(username);

        // Throw exception if user not found
        if (user == null) {
            throw new UserNotFoundException(username);
        }

        // Return found user
        return user;
    }

    @Override
    public void sendFollowRequest(String username, String followUsername) {
        // User and user to be followed
        User user = findUserByName(username);
        User followUser = findUserByName(followUsername);

        // Check if the user is trying to follow themselves
        if (username.equals(followUsername)) {
            throw new IllegalArgumentException("Users cannot follow themselves");
        }

        // Check if the user is already following the user to be followed
        if (user.getFollowing().contains(followUsername)) {
            throw new IllegalStateException(String.format("%s already follows %s.", username, followUsername));
        }

        // Existing follow request to the user to be followed has been sent
        if (followUser.getFollowRequests().contains(username)) {
            throw new IllegalStateException("Follow request already sent ");
        }

        // Send follow request
        followUser.addFollowRequest(username);
        userRepository.save(followUser);
    }

    @Override
    public void acceptFollowRequest(String username, String followUsername) {
        // User and user to be followed
        User user = findUserByName(username);
        User followUser = findUserByName(followUsername);

        // If follow request is not found/ does not exist
        if (!user.getFollowRequests().contains(followUsername)) {
            // Throw exception
            throw new IllegalStateException(String.format("No follow request from %s to %s found",
                    followUsername, username));
        }

        // Add "followUsername" to user's followers list
        user.addFollower(followUsername);

        // Add "username" to followUser's following list
        followUser.addFollowing(username);

        // Remove the follow request
        user.removeFollowRequest(followUsername);

        userRepository.save(user);
        userRepository.save(followUser);
    }

    @Override
    public void rejectFollowRequest(String username, String rejectUsername) {
        // User and user to be followed
        User user = findUserByName(username);

        // If follow request is not found/ does not exist
        if (!user.getFollowRequests().contains(rejectUsername)) {
            // Throw exception
            throw new IllegalStateException(String.format("No follow request from %s to %s found",
                    rejectUsername, username));
        }

        // Remove the follow request
        user.removeFollowRequest(rejectUsername);

        userRepository.save(user);
    }

    @Override
    public void unfollowUser (String username, String unfollowUsername) {
        // User and user to be unfollowed
        User user = findUserByName(username);
        User unfollowUser = findUserByName(unfollowUsername);

        // User is not following the user to be unfollowed
        if (!user.getFollowing().contains(unfollowUsername)) {
            throw new IllegalStateException(String.format("%s is not following %s", username, unfollowUsername));
        }

        // Remove "unfollowUsername" from following list of user
        user.removeFollowing(unfollowUsername);

        // Remove "user" from followers list of user being unfollowed
        unfollowUser.removeFollower(username);

        userRepository.save(user);
        userRepository.save(unfollowUser);
    }

    @Override
    public ArrayList<String> getFollowers(String username) {
        // Find user by username
        User user = findUserByName(username);

        // Return list of followers
        return user.getFollowers();
    }

    @Override
    public ArrayList<String> getFollowing(String username) {
        // Find user by username
        User user = findUserByName(username);

        // Return list of following
        return user.getFollowing();
    }

    @Override
    public ArrayList<String> getFollowRequests(String username) {
        // Find user by username
        User user = findUserByName(username);

        // Return list of follow requests
        return user.getFollowRequests();
    }

    @Override
    public void removeFollower(String username, String removeFollower) {
        // User and user to be removed
        User user = findUserByName(username);
        User removeUser = findUserByName(removeFollower);

        // User is not following the user to be removed
        if (!user.getFollowers().contains(removeFollower)) {
            throw new IllegalStateException(String.format("%s is not following %s", removeFollower, username));
        }

        // Remove "removeFollower" from followers list of user
        user.removeFollower(removeFollower);

        // Remove "user" from following list of user being removed
        removeUser.removeFollowing(username);

        userRepository.save(user);
        userRepository.save(removeUser);
    }

}
