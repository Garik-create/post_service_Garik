package faang.school.postservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import faang.school.postservice.config.context.UserContext;
import faang.school.postservice.publishers.PostViewPublisher;
import faang.school.postservice.events.PostViewEvent;
import faang.school.postservice.model.Post;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostViewEventService {
    private final PostViewPublisher messagePublisher;
    private final ObjectMapper objectMapper;
    private final UserContext userContext;

    public void publishViewEvent(Post post)  {
        try {
            PostViewEvent postViewEvent = new PostViewEvent();
            postViewEvent.setUserId(getUserId());
            postViewEvent.setPostId(post.getId());
            postViewEvent.setAuthorId(post.getAuthorId());
            postViewEvent.setViewedAt(LocalDateTime.now());
            String message = objectMapper.writeValueAsString(postViewEvent);
            messagePublisher.publish(message);
        } catch (JsonProcessingException e) {
            log.warn("Failure occurred withing converting PostViewEvent with id {} to String",post.getId(),e);
        }
    }

    private long getUserId(){
        return userContext.getUserId();
    }
}