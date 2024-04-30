package faang.school.postservice.repository;

import faang.school.postservice.model.Post;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findByAuthorId(long authorId);

    List<Post> findByProjectId(long projectId);

    @Query("SELECT p.authorId, COUNT(p) FROM Post p WHERE p.verified = false " +
            "AND p.authorId IS NOT NULL GROUP BY p.authorId HAVING COUNT(p) > :count")
    List<Long> findAuthorIdsByNotVerifiedPosts(int count);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes WHERE p.projectId = :projectId")
    List<Post> findByProjectIdWithLikes(long projectId);

    @Query("SELECT p FROM Post p LEFT JOIN FETCH p.likes WHERE p.authorId = :authorId")
    List<Post> findByAuthorIdWithLikes(long authorId);

    @Query("SELECT p FROM Post p WHERE p.published = false AND p.deleted = false AND p.scheduledAt <= CURRENT_TIMESTAMP")
    List<Post> findReadyToPublish();

    @Query("SELECT s.follower.id FROM Subscription s WHERE s.followee.id = :authorId")
    List<Long> findFollowerIdsByAuthorId(long authorId);

    List<Post> findTopNByUserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
}
