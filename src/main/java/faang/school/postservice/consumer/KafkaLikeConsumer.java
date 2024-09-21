package faang.school.postservice.consumer;

import faang.school.postservice.dto.event.LikePostEvent;
import faang.school.postservice.dto.event.kafka.PostLikeEvent;
import faang.school.postservice.service.FeedService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KafkaLikeConsumer extends AbstractConsumer<PostLikeEvent> {

    public KafkaLikeConsumer(FeedService feedService) {
        super(feedService);
    }

    @Override
    @KafkaListener(topics = "${spring.kafka.topic.like-post}", groupId = "${spring.kafka.consumer.group-id}")
    public void listen(PostLikeEvent event) {
         log.info("New post event received: {}", event);
         feedService.addLikeToPost(event.getPostId());
    }
}
