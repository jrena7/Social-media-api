package com.example.socialmedia.service.impl;

import com.example.socialmedia.exception.PostNotFoundException;
import com.example.socialmedia.exception.UnauthorisedAccessException;
import com.example.socialmedia.model.Post;
import com.example.socialmedia.model.User;
import com.example.socialmedia.repository.PostRepository;
import com.example.socialmedia.repository.UserRepository;
import com.example.socialmedia.service.PostService;
import com.example.socialmedia.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class PostServiceImp implements PostService {

    // Directory where all the images will be stored
    private final String DIRECTORY = "uploads/images/";

    private final UserRepository userRepository;

    private final UserService userService;

    private final PostRepository postRepository;

    @Autowired
    public PostServiceImp(UserRepository userRepository, UserService userService, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.userService = userService;
        this.postRepository = postRepository;
    }

    @Transactional
    @Override
    public void uploadPost(String username, String caption, MultipartFile file) {

        // Make directory if it doesn't exist
        Path directory = Paths.get(DIRECTORY);
        if (!Files.exists(directory)) {
            try {
                Files.createDirectories(directory);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        try {
            // Make the image name unique
            String postName = UUID.randomUUID() + "_" + file.getOriginalFilename();

            // Save the image to the directory
            Path postPath = directory.resolve(postName);
            Files.write(postPath, file.getBytes());

            User user = userService.findUserByName(username);

            // Create the URL for the image
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/api/v1/posts/" + username + "/")
                    .path(postName)
                    .toUriString();

            // Create the post and save it to the database
            Post post = new Post(url, user.getId(), caption);
            postRepository.save(post);

            // Add the post to the user's list of posts + save
            user.addPost(post.getPostId());
            userRepository.save(user);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Post> getUserPosts(String username, String postsOf) {
        User user = userService.findUserByName(username);

        // Can only view posts of users that you are following/ your own posts
        if (!user.isFollowing(postsOf) && !user.getUsername().equals(postsOf)) {
            throw new IllegalStateException(username + " is not following " + postsOf);
        }

        // Get the user's posts
        return postRepository.findAllByUserId(user.getId());
    }

    @Override
    public Resource getImage(String filename) {
        // Get the file path of the image
        Path filePath = Paths.get(DIRECTORY + filename);

        // Return the image as a resource
        Resource resource;
        try {
            resource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        return resource;
    }

    @Override
    public void deletePost(String username, String postId) {
        User user = userService.findUserByName(username);

        // Ensure that the user is the creator of the post
        Post post = isPostOwner(user, postId);

        // Remove the post from the user's list of posts + save
        user.removePost(postId);
        postRepository.delete(post);
        userRepository.save(user);
    }

    @Override
    public void editCaption(String username, String postId, String newCaption) {
        User user = userService.findUserByName(username);
        Post post = isPostOwner(user, postId);

        // Update the caption of the post + save
        post.setCaption(newCaption);
        postRepository.save(post);
    }

    // Helper Methods
    private Post isPostOwner(User user, String postId) {
        Post post = findPostById(postId);

        // Ensure that the user is the creator of the post
        if (!user.getPosts().contains(postId)) {
            throw new UnauthorisedAccessException(user.getUsername());
        }

        return post;
    }

    // Ensures null post is returned
    private Post findPostById(String postId) {
        Post post = postRepository.findByPostId(postId);

        if (post == null) {
            throw new PostNotFoundException(postId);
        }

        return post;
    }
}
