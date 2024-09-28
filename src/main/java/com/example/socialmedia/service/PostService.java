package com.example.socialmedia.service;


import com.example.socialmedia.model.Post;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PostService {
    void uploadPost(String username, String caption, MultipartFile file);

    List<Post> getUserPosts(String username, String postsOf);

    Resource getImage(String filename);

    void deletePost(String username, String postId);

    void editCaption(String username, String postId, String newCaption);
}
