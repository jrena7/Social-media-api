package com.example.socialmedia.service;

import com.example.socialmedia.model.User;
import java.util.ArrayList;

public interface UserService {
    void createUser(User userToCreate);

    User findUserByName(String username);

    void sendFollowRequest(String username, String followUsername);

    void acceptFollowRequest(String username, String followUsername);

    void rejectFollowRequest(String username, String rejectUsername);

    void unfollowUser(String username, String followUsername);

    ArrayList<String> getFollowers(String username);

    ArrayList<String> getFollowing(String username);

    ArrayList<String> getFollowRequests(String username);

    void removeFollower(String username, String removeFollower);
}
