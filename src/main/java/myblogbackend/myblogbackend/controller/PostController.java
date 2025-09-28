package myblogbackend.myblogbackend.controller;
import myblogbackend.myblogbackend.entity.Post;
import myblogbackend.myblogbackend.entity.User;
import myblogbackend.myblogbackend.service.PostService;
import myblogbackend.myblogbackend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/posts")
@CrossOrigin(origins = "http://localhost:3000")
public class PostController {
    
    @Autowired
    private PostService postService;
    
    @Autowired
    private UserService userService;
    
    @PostMapping("/create")
    public ResponseEntity<Map<String, Object>> createPost(@Valid @RequestBody Map<String, Object> postRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Long authorId = Long.valueOf(postRequest.get("authorId").toString());
            String title = postRequest.get("title").toString();
            String content = postRequest.get("content").toString();
            
            User author = userService.findById(authorId)
                .orElseThrow(() -> new Exception("Author not found"));
            
            Post post = new Post(title, content, author);
            Post savedPost = postService.createPost(post);
            
            response.put("success", true);
            response.put("message", "Post created successfully");
            response.put("post", formatPostResponse(savedPost));
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @GetMapping("/all")
    public ResponseEntity<Map<String, Object>> getAllPosts() {
        Map<String, Object> response = new HashMap<>();
        
        try {
            List<Post> posts = postService.getAllPosts();
            List<Map<String, Object>> postResponses = posts.stream()
                .map(this::formatPostResponse)
                .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("posts", postResponses);
            response.put("count", posts.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<Map<String, Object>> getPostsByUser(@PathVariable Long userId) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            User author = userService.findById(userId)
                .orElseThrow(() -> new Exception("User not found"));
            
            List<Post> posts = postService.getPostsByAuthor(author);
            List<Map<String, Object>> postResponses = posts.stream()
                .map(this::formatPostResponse)
                .collect(Collectors.toList());
            
            response.put("success", true);
            response.put("posts", postResponses);
            response.put("count", posts.size());
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getPostById(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Post post = postService.getPostById(id)
                .orElseThrow(() -> new Exception("Post not found"));
            
            response.put("success", true);
            response.put("post", formatPostResponse(post));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updatePost(@PathVariable Long id, @Valid @RequestBody Map<String, Object> postRequest) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            Post existingPost = postService.getPostById(id)
                .orElseThrow(() -> new Exception("Post not found"));
            
            if (postRequest.containsKey("title")) {
                existingPost.setTitle(postRequest.get("title").toString());
            }
            if (postRequest.containsKey("content")) {
                existingPost.setContent(postRequest.get("content").toString());
            }
            
            Post updatedPost = postService.updatePost(existingPost);
            
            response.put("success", true);
            response.put("message", "Post updated successfully");
            response.put("post", formatPostResponse(updatedPost));
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deletePost(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();
        
        try {
            if (!postService.getPostById(id).isPresent()) {
                throw new Exception("Post not found");
            }
            
            postService.deletePost(id);
            
            response.put("success", true);
            response.put("message", "Post deleted successfully");
            return ResponseEntity.ok(response);
            
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }
    
    private Map<String, Object> formatPostResponse(Post post) {
        Map<String, Object> postMap = new HashMap<>();
        postMap.put("id", post.getId());
        postMap.put("title", post.getTitle());
        postMap.put("content", post.getContent());
        postMap.put("createdAt", post.getCreatedAt());
        postMap.put("updatedAt", post.getUpdatedAt());
        
        Map<String, Object> authorMap = new HashMap<>();
        authorMap.put("id", post.getAuthor().getId());
        authorMap.put("username", post.getAuthor().getUsername());
        authorMap.put("email", post.getAuthor().getEmail());
        postMap.put("author", authorMap);
        
        return postMap;
    }
}