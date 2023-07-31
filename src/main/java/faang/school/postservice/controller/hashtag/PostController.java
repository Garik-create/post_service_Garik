package faang.school.postservice.controller.hashtag;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.service.post.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/hashtags")
public class PostController {
    private final PostService postService;

    @GetMapping("/{hashtag}")
    public List<PostDto> getByHashtag(@PathVariable String hashtag){
        return postService.getPostsByHashtag(hashtag);
    }
}
