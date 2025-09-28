package myblogbackend.myblogbackend.repository;

import myblogbackend.myblogbackend.entity.Post;
import myblogbackend.myblogbackend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    // Get all posts by author
    List<Post> findByAuthorOrderByCreatedAtDesc(User author);

    // Get all posts sorted by createdAt (desc)
    List<Post> findAllByOrderByCreatedAtDesc();

    // Search by title or content
    List<Post> findByTitleContainingOrContentContainingOrderByCreatedAtDesc(
            String titleKeyword,
            String contentKeyword
    );

    // Check if a user has already posted the same title + content
    Optional<Post> findByAuthorAndTitleAndContent(User author, String title, String content);
}
