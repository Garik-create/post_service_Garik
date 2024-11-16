package faang.school.postservice.service.impl;

import faang.school.postservice.model.Post;
import faang.school.postservice.repository.CachePostRepository;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.service.AsyncPostPublishService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AsyncPostPublishServiceImpl implements AsyncPostPublishService {

    private final PostRepository postRepository;
    private final CachePostRepository cachePostRepository;

    @Async("publishedPostThreadPool")
    public void publishPost(List<Post> posts) {
        posts.forEach(post -> {
            post.setPublished(true);
            post.setPublishedAt(LocalDateTime.now());
            cachePostRepository.save(post.getAuthorId().toString(), post.getId());
        });
        postRepository.saveAll(posts);
    }
}