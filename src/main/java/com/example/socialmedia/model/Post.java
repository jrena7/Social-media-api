package com.example.socialmedia.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "posts")
public class Post {

    @Id
    private String postId;

    private String userId;

    private String imageUrl;

    private String caption;

    private LocalDateTime createdAt;

    public Post(String imageUrl, String userId, String caption) {
        this.imageUrl = imageUrl;
        this.userId = userId;
        this.caption = caption;
        this.createdAt = LocalDateTime.now();
    }
}
