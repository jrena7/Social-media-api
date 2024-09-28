package com.example.socialmedia.controller;

import com.example.socialmedia.model.Post;
import com.example.socialmedia.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    @Autowired
    public PostController(PostService postService) {
        this.postService = postService;
    }


    @PostMapping("/{username}/upload")
    public ResponseEntity<?> uploadPost(@RequestParam("file") MultipartFile file, @PathVariable String username, @RequestParam String caption) {
        // Check the file type
        validFileType(file);

        // Upload the post to the server
        postService.uploadPost(username, caption, file);
        return ResponseEntity.ok("Post uploaded successfully.");
    }

    @GetMapping("/{username}")
    public ResponseEntity<List<Post>> getUserPosts(@PathVariable String username, @RequestParam String postsOf) {
        List<Post> posts = postService.getUserPosts(username, postsOf);

        // Return a list of posts that a user is following
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/{username}/{filename}")
    public ResponseEntity<?> getImage(@PathVariable String username, @PathVariable String filename) {
        Resource resource = postService.getImage(filename);

        // Return the image as a resource for the user to view
        return ResponseEntity.ok()
                .contentType(getMediaType(filename))
                .body(resource);

    }

    @PutMapping("/{username}/edit")
    public ResponseEntity<?> editPost(@PathVariable String username, @RequestParam String postId, @RequestBody String caption) {
        postService.editCaption(username, postId, caption);
        return ResponseEntity.ok("Post edited successfully.");
    }

    @DeleteMapping("/{username}/delete")
    public ResponseEntity<?> deletePost(@PathVariable String username, @RequestParam String postId) {
        postService.deletePost(username, postId);
        return ResponseEntity.ok("Post deleted successfully.");
    }

    // Returns the media type of the file
    private MediaType getMediaType(String filename) {
        // Get the file extension
        String[] parts = filename.split("\\.");
        String extension = parts[parts.length - 1];

        // Match the extension to the media type
        return switch (extension) {
            case "png" -> MediaType.IMAGE_PNG;
            case "jpg", "jpeg" -> MediaType.IMAGE_JPEG;
            default -> MediaType.APPLICATION_OCTET_STREAM;
        };
    }

    // Method to ensure file type is PNG or JPEG
    private void validFileType (MultipartFile file) {
        String filename = file.getOriginalFilename();

        if (filename == null) {
            throw new IllegalArgumentException("Invalid file type. Only PNG and JPEG files are supported.");
        }

        MediaType type = getMediaType(filename);

        if (!type.equals(MediaType.IMAGE_PNG) && !type.equals(MediaType.IMAGE_JPEG)) {
            throw new IllegalArgumentException("Invalid file type. Only PNG and JPEG files are supported.");
        }
    }
}
