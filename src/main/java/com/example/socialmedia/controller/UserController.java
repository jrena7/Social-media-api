package com.example.socialmedia.controller;

import com.example.socialmedia.service.UserService;
import com.example.socialmedia.service.impl.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService, AuthService authService) {
        this.userService = userService;
    }

    @PostMapping("/{username}/followers/follow")
    public ResponseEntity<?> sendFollowRequest(@PathVariable String username, @RequestParam String followUsername) {

        // Follow user
        userService.sendFollowRequest(username, followUsername);

        // Return success message
        return ResponseEntity.ok("Follow request sent successfully.");
    }

    @PostMapping("/{username}/followers/accept")
    public ResponseEntity<?> acceptFollowRequest (@PathVariable String username, @RequestParam String followUsername) {

        // Accept follow request
        userService.acceptFollowRequest(username, followUsername);

        // Return success message
        return ResponseEntity.ok("Follow request accepted.");
    }

    @PostMapping("/{username}/followers/reject")
    public ResponseEntity<?> rejectFollowRequest (@PathVariable String username, @RequestParam String followUsername) {

        // Reject follow request
        userService.rejectFollowRequest(username, followUsername);

        // Return success message
        return ResponseEntity.ok("Follow request rejected.");
    }

    @PostMapping("/{username}/followers/unfollow")
    public ResponseEntity<?> unfollowUser (@PathVariable String username, @RequestParam String unfollowUsername) {

        // Unfollow user
        userService.unfollowUser(username, unfollowUsername);

        // Return success message
        return ResponseEntity.ok(String.format("Successfully unfollowed %s", unfollowUsername));
    }


    @GetMapping("/{username}/followers")
    public ResponseEntity<?> getFollowers(@PathVariable String username) {

        // Get user followers
        ArrayList<String> followers = userService.getFollowers(username);

        // Return user followers on success
        return new ResponseEntity<>(followers, HttpStatus.OK);
    }

    @GetMapping("/{username}/following")
    public ResponseEntity<?> getFollowing(@PathVariable String username) {

        // Get user followers
        ArrayList<String> following = userService.getFollowing(username);

        // Return user followers on success
        return new ResponseEntity<>(following, HttpStatus.OK);
    }

    @GetMapping("/{username}/followRequests")
    public ResponseEntity<?> getFollowerRequests(@PathVariable String username) {

        // Get user followers
        ArrayList<String> followerRequests = userService.getFollowRequests(username);

        // Return user followers on success
        return new ResponseEntity<>(followerRequests, HttpStatus.OK);
    }


    @PostMapping("/{username}/followers/remove")
    public ResponseEntity<?> removeFollower(@PathVariable String username, @RequestParam String removeFollower) {

        // Remove follower
        userService.removeFollower(username, removeFollower);

        // Return success message
        return ResponseEntity.ok(String.format("Successfully removed %s from followers", removeFollower));
    }
}
