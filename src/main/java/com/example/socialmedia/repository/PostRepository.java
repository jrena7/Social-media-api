package com.example.socialmedia.repository;

import com.example.socialmedia.model.Post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, String> {
    List<Post> findAllByUserId(String userId);

    Post findByPostId(String postId);
}
