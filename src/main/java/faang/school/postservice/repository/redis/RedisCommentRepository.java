package faang.school.postservice.repository.redis;

import faang.school.postservice.model.redis.RedisComment;
import org.springframework.data.repository.CrudRepository;

public interface RedisCommentRepository extends CrudRepository<RedisComment, String> {

}
