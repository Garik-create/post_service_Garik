package faang.school.postservice.model.redis;

import lombok.Data;

@Data
public class RedisComment {
    private String id;
    private String comment;
    private long createdAt;
}
