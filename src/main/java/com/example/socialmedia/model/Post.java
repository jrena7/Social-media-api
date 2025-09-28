package com.example.socialmedia.model;

import lombok.Data;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "posts")
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
