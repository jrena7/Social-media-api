package com.example.socialmedia.model;

import lombok.Data;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Data
@Entity
@Table(name = "users")
public class User {

    @Id
    private String id;

    @Column(unique = true)
    private String email;

    @Column(unique = true)
    private String username;

    private String password;

    @ElementCollection
    private ArrayList<String> followers;

    @ElementCollection
    private ArrayList<String> following;

    @ElementCollection
    private ArrayList<String> followRequests;

    @ElementCollection
    private ArrayList<String> chats;

    @ElementCollection
    private ArrayList<String> posts;

    private LocalDateTime createdAt;

    public User(String email, String username, String password) {
        this.email = email;
        this.username = username;
        this.password = password;
        this.followers = new ArrayList<>();
        this.following = new ArrayList<>();
        this.followRequests = new ArrayList<>();
        this.chats = new ArrayList<>();
        this.posts = new ArrayList<>();
        this.createdAt = LocalDateTime.now();
    }

    public void addFollower(String follower) {
        this.followers.add(follower);
    }

    public void removeFollower(String follower) {
        this.followers.remove(follower);
    }

    public void addFollowing(String following) {
        this.following.add(following);
    }

    public void removeFollowing(String following) {
        this.following.remove(following);
    }

    public Boolean isFollowing(String following) {
        return this.following.contains(following);
    }

    public void addFollowRequest(String followRequest) {
        this.followRequests.add(followRequest);
    }

    public void removeFollowRequest(String followRequest) {
        this.followRequests.remove(followRequest);
    }

    public void addChat(String chat) {
        this.chats.add(chat);
    }

    public void removeChat(String chat) {
        this.chats.remove(chat);
    }

    public void addPost(String postId) {
        this.posts.add(postId);
    }

    public void removePost(String postId) {
        this.posts.remove(postId);
    }
}
