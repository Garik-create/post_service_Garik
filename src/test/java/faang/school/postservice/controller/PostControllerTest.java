package faang.school.postservice.controller;

import faang.school.postservice.dto.post.PostDto;
import faang.school.postservice.exception.DataValidationException;
import faang.school.postservice.service.Post.PostService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(SpringExtension.class)
class PostControllerTest {
    @Mock
    private PostService postService;

    @InjectMocks
    private PostController postController;

    private PostDto postDto;
    private Long id = 1L;

    @BeforeEach
    void setUp() {
        postDto = new PostDto();
        postDto.setId(1L);
        postDto.setContent("content");
    }

    @Test
    void testPostCreateByUser() {
        postDto.setAuthorId(2L);
        postDto.setContent("content");

        postController.create(postDto);
        verify(postService, times(1)).create(postDto);
    }

    @Test
    void testPostCreateByProject() {
        postDto.setProjectId(2L);

        postController.create(postDto);
        verify(postService, times(1)).create(postDto);
    }

    @Test
    void testPostCreateByNulls() {
        assertThrows(DataValidationException.class, () -> postController.create(postDto));
    }

    @Test
    void testCreateTestByBoth() {
        postDto.setProjectId(2L);
        postDto.setAuthorId(2L);

        assertThrows(DataValidationException.class, () -> postController.create(postDto));
    }

    @Test
    void testCreateEmptyPost() {
        postDto.setAuthorId(2L);
        postDto.setContent(" ");

        assertThrows(DataValidationException.class, () -> postController.create(postDto));

        postDto.setContent("");

        assertThrows(DataValidationException.class, () -> postController.create(postDto));
    }


    @Test
    void testPublish() {
        postController.publish(id);
        verify(postService, times(1)).publish(id);
    }

    @Test
    void testDelete() {
        postController.delete(id);
        verify(postService, times(1)).delete(id);
    }

    @Test
    void testUpdate() {
        postController.update(postDto, id);
        verify(postService, times(1)).update(postDto, id);
    }

    @Test
    void testGet() {
        postController.get(id);
        verify(postService, times(1)).get(id);
    }

    @Test
    void testGetAllByAuthorId() {
        postController.getAllNonPublishedByAuthorId(id);
        verify(postService, times(1)).getAllNonPublishedByAuthorId(id);
    }

    @Test
    void testGetAllByProjectId() {
        postController.getAllNonPublishedByProjectId(id);
        verify(postService, times(1)).getAllNonPublishedByProjectId(id);
    }

    @Test
    void testGetAllPublishedByAuthorId() {
        postController.getAllPublishedByAuthorId(id);
        verify(postService, times(1)).getAllPublishedByAuthorId(id);
    }

    @Test
    void testGetAllPublishedByProjectId() {
        postController.getAllPublishedByProjectId(id);
        verify(postService, times(1)).getAllPublishedByProjectId(id);
    }
}