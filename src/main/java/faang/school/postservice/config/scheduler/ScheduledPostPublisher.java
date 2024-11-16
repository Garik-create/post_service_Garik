package faang.school.postservice.config.scheduler;

import faang.school.postservice.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ScheduledPostPublisher {

    private final PostService postService;

    @Scheduled(cron = "${post.publisher.scheduler.cron}")
    public void postPublisher() {
        postService.publishScheduledPosts();
    }
}