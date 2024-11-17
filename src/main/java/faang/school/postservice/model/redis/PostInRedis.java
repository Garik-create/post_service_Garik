package faang.school.postservice.model.redis;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

@Data
@RedisHash(value = "post", timeToLive = 86400L)
public class PostInRedis implements Serializable {

    @Id
    private String id;
    private String content;
    private long authorId;
    private long projectId;
    private AtomicLong numberOfLikes;
    private List<String> redisComment;

}
