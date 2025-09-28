package myblogbackend.myblogbackend.service;

import myblogbackend.myblogbackend.entity.Post;
import myblogbackend.myblogbackend.entity.User;
import myblogbackend.myblogbackend.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    // Create a post, preventing duplicate posts by same user
    public Post createPost(Post post) throws Exception {
        Optional<Post> existingPost = postRepository.findByAuthorAndTitleAndContent(
                post.getAuthor(), post.getTitle(), post.getContent()
        );
        if (existingPost.isPresent()) {
            throw new Exception("You have already posted this content");
        }
        return postRepository.save(post);
    }

    public List<Post> getAllPosts() {
        return postRepository.findAllByOrderByCreatedAtDesc();
    }

    public List<Post> getPostsByAuthor(User author) {
        return postRepository.findByAuthorOrderByCreatedAtDesc(author);
    }

    public Optional<Post> getPostById(Long id) {
        return postRepository.findById(id);
    }

    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        postRepository.deleteById(id);
    }

    public List<Post> searchPosts(String keyword) {
        return postRepository.findByTitleContainingOrContentContainingOrderByCreatedAtDesc(keyword, keyword);
    }
}
