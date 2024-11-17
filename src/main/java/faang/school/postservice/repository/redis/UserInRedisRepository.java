package faang.school.postservice.repository.redis;

import faang.school.postservice.model.redis.UserInRedis;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserInRedisRepository extends CrudRepository<UserInRedis, String> {

}
