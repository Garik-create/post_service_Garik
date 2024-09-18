package faang.school.postservice.service.Post;

import faang.school.postservice.client.ProjectServiceClient;
import faang.school.postservice.client.UserServiceClient;
import faang.school.postservice.config.context.UserContext;
import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.exception.DataValidationException;
import faang.school.postservice.mapper.PostMapper;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final PostMapper postMapper;
    private final UserServiceClient userServiceClient;
    private final ProjectServiceClient projectServiceClient;
    private final UserContext userContext;

    @Override
    public PostDto create(PostDto postDto) {
        if (postDto.getAuthorId() != null) {
            userContext.setUserId(postDto.getAuthorId());

            userServiceClient.getUser(postDto.getAuthorId());
        } else {
            projectServiceClient.getProject(postDto.getProjectId());
        }

        Post post = postMapper.toEntity(postDto);
        post.setPublished(false);
        post.setDeleted(false);

        postRepository.save(post);
        return postDto;
    }

    @Override
    public PostDto publish(Long id) {
        Post post = postRepository.findById(id).orElseThrow(DataValidationException::new);

        if (post.isPublished()) {
            throw new DataValidationException();
        }
        post.setPublished(true);
        post.setPublishedAt(LocalDateTime.now());

        postRepository.save(post);
        return postMapper.toDto(post);
    }

    @Override
    public PostDto update(PostDto postDto, Long id) {
        Post post = postRepository.findById(id).orElseThrow(DataValidationException::new);

        post.setUpdatedAt(LocalDateTime.now());
        post.setContent(postDto.getContent());

        postRepository.save(post);

        return postMapper.toDto(post);
    }

    @Override
    public PostDto delete(Long id) {
        Post post = postRepository.findById(id).orElseThrow(DataValidationException::new);
        if (post.isDeleted()) {
            throw new DataValidationException();
        }

        post.setPublished(false);
        post.setDeleted(true);
        postRepository.save(post);

        PostDto postDto = postMapper.toDto(post);
        postDto.setDeletedAt(LocalDateTime.now());

        return postDto;
    }

    @Override
    public PostDto get(Long id) {
        Post post = postRepository.findById(id).orElseThrow(DataValidationException::new);
        return postMapper.toDto(post);
    }

    @Override
    public List<PostDto> getAllNonPublishedByAuthorId(Long id) {
        userServiceClient.getUser(id);

        return postRepository.findByAuthorId(id).stream()
                .filter(post -> !post.isDeleted() && !post.isPublished())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostDto> getAllNonPublishedByProjectId(Long id) {
        projectServiceClient.getProject(id);

        return postRepository.findByProjectId(id).stream()
                .filter(post -> !post.isDeleted() && !post.isPublished())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostDto> getAllPublishedByAuthorId(Long id) {
        userServiceClient.getUser(id);

        return postRepository.findByAuthorId(id).stream()
                .filter(post -> !post.isDeleted() && post.isPublished())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(postMapper::toDto)
                .toList();
    }

    @Override
    public List<PostDto> getAllPublishedByProjectId(Long id) {
        projectServiceClient.getProject(id);

        return postRepository.findByProjectId(id).stream()
                .filter(post -> !post.isDeleted() && post.isPublished())
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(postMapper::toDto)
                .toList();
    }
}