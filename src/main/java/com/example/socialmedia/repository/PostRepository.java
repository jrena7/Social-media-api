package com.example.socialmedia.repository;

import com.example.socialmedia.model.Post;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface PostRepository extends MongoRepository<Post, String> {
    List<Post> findAllByUserId(String userId);

    Post findByPostId(String postId);
}
