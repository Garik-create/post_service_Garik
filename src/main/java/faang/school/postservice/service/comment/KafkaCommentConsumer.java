package faang.school.postservice.service.comment;

import faang.school.postservice.mapper.PostMapper;
import faang.school.postservice.model.Post;
import faang.school.postservice.model.kafka.KafkaCommentEvent;
import faang.school.postservice.model.redis.PostInRedis;
import faang.school.postservice.repository.PostRepository;
import faang.school.postservice.repository.redis.PostInRedisRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.collections.DefaultRedisZSet;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class KafkaCommentConsumer {

    private final PostInRedisRepository postInRedisRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final RedisOperations<String, String> operations;
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    @Value("${comments-amount}")
    private int commentsAmount;

    @KafkaListener(topics = "comments",
            containerFactory = "commentKafkaListenerContainerFactory")
    public void receiveCommentEvent(KafkaCommentEvent event) {
        long postId = event.getPostId();
        Optional<PostInRedis> post = postInRedisRepository.findById(String.valueOf(postId));

        if (post.isPresent()) {
            PostInRedis postInRedis = post.get();
            savePostInRedis(postInRedis, event);
        } else {
            Post postInDb = postRepository.findById(postId).orElseThrow(
                    () -> new IllegalArgumentException("Такого поста не существует"));
            PostInRedis postInRedis2 = postInRedisRepository.save(
                    postMapper.entityToPostInRedis(postInDb));
            savePostInRedis(postInRedis2, event);
        }
    }

    private void savePostInRedis(PostInRedis postInRedis, KafkaCommentEvent event) {
        String postKey = postInRedis.getId();
        redisTemplate.watch(postKey);
        DefaultRedisZSet<String> comments = new DefaultRedisZSet<>(postKey,
                operations, 0);
        comments.add(event.getContent(),
                event.getCreatedAt().toInstant(ZoneOffset.UTC).toEpochMilli());
        redisTemplate.multi();
        List<String> commentsList = comments.reverseRange(0, commentsAmount - 1)
                .stream()
                .toList();
        postInRedis.setRedisComment(commentsList);
        postInRedisRepository.save(postInRedis);
        redisTemplate.exec();
        log.info("Комментарий сохранён в  пост {} ", postKey);
    }
}
