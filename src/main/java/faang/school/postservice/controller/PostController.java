package faang.school.postservice.controller;

import faang.school.postservice.dto.post.PostDraftCreateDto;
import faang.school.postservice.dto.post.PostDraftResponseDto;
import faang.school.postservice.dto.post.PostPublishResponseDto;
import faang.school.postservice.dto.post.PostUpdateDto;
import faang.school.postservice.service.post.PostService;
//import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/posts")
@Validated
public class PostController {
    private final PostService postService;

    //    @Operation(summary = "Create draft post")
    @PostMapping("/draft")
    public PostDraftResponseDto createDraftPost(@RequestBody @Valid PostDraftCreateDto dto) {
        return postService.createDraftPost(dto);
    }

    //    @Operation(summary = "Publish post by ID")
    @PutMapping("/{postId}/publish")
    public PostPublishResponseDto publishPost(@PathVariable @Positive long postId) {
        return postService.publishPost(postId);
    }

    //    @Operation(summary = "Update post")
    @PutMapping("/{postId}/update")
    public PostPublishResponseDto updatePost(@PathVariable @Positive long postId,
                                              @RequestBody @Valid PostUpdateDto dto) {
        return postService.updatePost(postId, dto);
    }

    //    @Operation(summary = "Hide post by ID")
    @DeleteMapping("/{postId}/delete")
    public void deletePostById(@PathVariable @Positive long postId) {
        postService.deletePostById(postId);
    }

    //    @Operation(summary = "Get post by ID")
    @GetMapping("/{postId}")
    public PostPublishResponseDto getPostById(@PathVariable @Positive long postId) {
        return postService.getPostById(postId);
    }

    //    @Operation(summary = "Get all drafts of non-del posts authored by the user and sorted by creation date from new to old.")
    @GetMapping("/user/{userId}")
    public List<PostDraftResponseDto> getAllDraftNonDelPostsByUserIdSortedCreatedAtDesc(@PathVariable @Positive long userId) {
        return postService.getAllDraftNonDelPostsByUserIdSortedCreatedAtDesc(userId);
    }

    //    @Operation(summary = "Get all drafts of non-del posts authored by the project and sorted by creation date from new to old.")
    @GetMapping("/project/{projectId}")
    public List<PostDraftResponseDto> getAllDraftNonDelPostsByProjectIdSortedCreatedAtDesc(@PathVariable @Positive long projectId) {
        return postService.getAllDraftNonDelPostsByProjectIdSortedCreatedAtDesc(projectId);
    }

    //    @Operation(summary = "Get all publish of non-del posts authored by the user and sorted by creation date from new to old.")
    @GetMapping("/user/{userId}")
    public List<PostPublishResponseDto> getAllPublishNonDelPostsByUserIdSortedCreatedAtDesc(@PathVariable @Positive long userId) {
        return postService.getAllPublishNonDelPostsByUserIdSortedCreatedAtDesc(userId);
    }

    //    @Operation(summary = "Get all publish of non-del posts authored by the project and sorted by creation date from new to old.")
    @GetMapping("/project/{projectId}")
    public List<PostPublishResponseDto> getAllPublishNonDelPostsByProjectIdSortedCreatedAtDesc(@PathVariable @Positive long projectId) {
        return postService.getAllPublishNonDelPostsByProjectIdSortedCreatedAtDesc(projectId);
    }


}
