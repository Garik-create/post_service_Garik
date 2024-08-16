package faang.school.postservice.service;

import faang.school.postservice.config.context.UserContext;
import faang.school.postservice.dto.AlbumDto;
import faang.school.postservice.dto.AlbumFilterDto;
import faang.school.postservice.filter.AlbumAuthorFilter;
import faang.school.postservice.filter.AlbumDescriptionFilter;
import faang.school.postservice.filter.AlbumFilter;
import faang.school.postservice.filter.AlbumFromDateFilter;
import faang.school.postservice.filter.AlbumTitleFilter;
import faang.school.postservice.filter.AlbumToDateFilter;
import faang.school.postservice.handler.EntityHandler;
import faang.school.postservice.filter.album.AlbumAuthorFilter;
import faang.school.postservice.filter.album.AlbumDescriptionFilter;
import faang.school.postservice.filter.album.AlbumFilter;
import faang.school.postservice.filter.album.AlbumFromDateFilter;
import faang.school.postservice.filter.album.AlbumTitleFilter;
import faang.school.postservice.filter.album.AlbumToDateFilter;
import faang.school.postservice.mapper.AlbumMapper;
import faang.school.postservice.model.Album;
import faang.school.postservice.model.Post;
import faang.school.postservice.repository.AlbumRepository;
import faang.school.postservice.validator.AlbumValidator;
import faang.school.postservice.validator.PostValidator;
import faang.school.postservice.validator.UserValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AlbumServiceTest {
    @Mock
    private UserContext userContext;
    @Mock
    private AlbumMapper albumMapper;
    @Mock
    private EntityHandler entityHandler;
    @Mock
    private UserValidator userValidator;
    @Mock
    private PostValidator postValidator;
    @Mock
    private AlbumValidator albumValidator;
    @Mock
    private AlbumRepository albumRepository;
    @Mock
    private List<AlbumFilter> albumFilterList;

    @InjectMocks
    private AlbumService albumService;

    private long userId;
    private long postId;
    private long albumId;
    private long authorId;
    private long requesterId;
    private Album album;
    private Post post;
    private AlbumDto albumDto;
    private AlbumFilterDto albumFilterDto;
    private List<AlbumFilter> albumFilterListImpl;

    @BeforeEach
    public void setUp() {
        userId = 1L;
        postId = 2L;
        albumId = 3L;
        authorId = 4L;
        requesterId = 5L;
        List<Long> allowedUserIds = List.of(1L, 2L, 3L, 4L, 5L);
        album = Album.builder()
                .authorId(authorId)
                .title("title")
                .posts(new ArrayList<>())
                .build();
        albumDto = AlbumDto.builder()
                .authorId(authorId)
                .title("title")
                .build();
        post = Post.builder()
                .id(postId)
                .allowedUserIds(allowedUserIds)
                .build();
        albumFilterDto = AlbumFilterDto.builder()
                .titlePattern("title")
                .build();
        albumFilterDto = new AlbumFilterDto();
        albumFilterListImpl = List.of(
                new AlbumAuthorFilter(),
                new AlbumDescriptionFilter(),
                new AlbumFromDateFilter(),
                new AlbumTitleFilter(),
                new AlbumToDateFilter()
        );

        lenient().when(userContext.getUserId()).thenReturn(requesterId);
        lenient().when(albumValidator.validateAlbumExistence(albumId)).thenReturn(album);
        lenient().when(albumFilterList.iterator()).thenReturn(albumFilterListImpl.iterator());
        lenient().when(albumValidator.isVisibleToRequester(requesterId, album)).thenReturn(true);
    }

    @Test
    @DisplayName("testing createAlbum method")
    void testCreateAlbum() {
        when(albumMapper.toEntity(albumDto)).thenReturn(album);
        when(albumRepository.save(album)).thenReturn(album);

        albumService.createAlbum(albumDto);
        verify(userContext, times(1)).getUserId();
        verify(userValidator, times(1)).validateUserExistence(requesterId);
        verify(userValidator, times(1)).validateFollowersExistence(albumDto.getAllowedUserIds());

        verify(userValidator, times(1)).validateUserExistence(authorId);
        verify(albumValidator, times(1))
                .validateAlbumTitleDoesNotDuplicatePerAuthor(requesterId, albumDto.getTitle());
                .validateAlbumTitleDoesNotDuplicatePerAuthor(authorId, albumDto.getTitle());
        verify(albumMapper, times(1)).toEntity(albumDto);
        verify(albumRepository, times(1)).save(album);
        verify(albumMapper, times(1)).toDto(album);
    }

    @Test
    @DisplayName("testing addPostToAlbum method")
    void addPostToAlbum() {
        when(entityHandler.getOrThrowException(eq(Album.class), eq(albumId), any())).thenReturn(album);
        when(entityHandler.getOrThrowException(eq(Post.class), eq(postId), any())).thenReturn(post);

        albumService.addPostToAlbum(authorId, postId, albumId);

        verify(entityHandler, times(1)).getOrThrowException(eq(Album.class), eq(albumId), any());
        verify(entityHandler, times(1)).getOrThrowException(eq(Post.class), eq(postId), any());
        verify(albumValidator, times(1)).validateAlbumBelongsToAuthor(authorId, album);
        albumService.addPostToAlbum(postId, albumId);
        verify(userContext, times(1)).getUserId();
        verify(albumValidator, times(1)).validateAlbumExistence(albumId);
        verify(postValidator, times(1)).validatePostExistence(postId);
        verify(albumValidator, times(1)).validateAlbumBelongsToRequester(requesterId, album);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("testing removePostFromAlbum method")
    void testRemovePostFromAlbum() {
        albumService.removePostFromAlbum(postId, albumId);
        verify(userContext, times(1)).getUserId();
        verify(albumValidator, times(1)).validateAlbumExistence(albumId);
        when(entityHandler.getOrThrowException(eq(Album.class), eq(albumId), any())).thenReturn(album);

        albumService.removePostFromAlbum(authorId, postId, albumId);

        verify(entityHandler, times(1)).getOrThrowException(eq(Album.class), eq(albumId), any());
        verify(postValidator, times(1)).validatePostExistence(postId);
        verify(albumValidator, times(1)).validateAlbumBelongsToRequester(requesterId, album);
        verify(albumRepository, times(1)).save(album);
    }

    @Test
    @DisplayName("testing addAlbumToFavourites method")
    void testAddAlbumToFavourites() {
        albumService.addAlbumToFavourites(albumId);
        verify(userContext, times(1)).getUserId();
        verify(userValidator, times(1)).validateUserExistence(requesterId);
        verify(albumValidator, times(1)).validateAlbumExistence(albumId);
        verify(albumValidator, times(1)).validateVisibilityToRequester(requesterId, album);
        verify(albumRepository, times(1)).addAlbumToFavorites(albumId, requesterId);
    }

    @Test
    @DisplayName("testing removeAlbumFromFavorites method")
    void testRemoveAlbumFromFavourites() {
        albumService.removeAlbumFromFavourites(albumId);
        verify(userContext, times(1)).getUserId();
        verify(userValidator, times(1)).validateUserExistence(requesterId);
        verify(albumValidator, times(1)).validateAlbumExistence(albumId);
        verify(albumRepository, times(1)).deleteAlbumFromFavorites(albumId, requesterId);
    }

    @Test
    @DisplayName("testing getAlbumById method")
    void testGetAlbumById() {
        when(entityHandler.getOrThrowException(eq(Album.class), eq(albumId), any())).thenReturn(album);

        albumService.getAlbumById(albumId);

        verify(entityHandler, times(1)).getOrThrowException(eq(Album.class), eq(albumId), any());
        verify(albumMapper, times(1)).toDto(album);
        verify(userContext, times(1)).getUserId();
        verify(albumValidator, times(1)).validateAlbumExistence(albumId);
        verify(albumValidator, times(1)).validateVisibilityToRequester(requesterId, album);
    }

    @Test
    @DisplayName("testing getAuthorFilteredAlbums method")
    void testGetAuthorFilteredAlbums() {
        when(albumRepository.findByAuthorId(authorId)).thenReturn(Stream.of(album));
        when(albumFilterList.iterator()).thenReturn(albumFilterListImpl.iterator());
        albumService.getAuthorFilteredAlbums(authorId, albumFilterDto);
        verify(userContext, times(1)).getUserId();
        verify(albumRepository, times(1)).findByAuthorId(authorId);
        verify(albumValidator).isVisibleToRequester(requesterId, album);
    }

    @Test
    @DisplayName("testing getAllFilteredAlbums method")
    void testGetAllFilteredAlbums() {
        when(albumRepository.findAll()).thenReturn(List.of(album));
        when(albumRepository.findAll()).thenReturn(List.of(album));
        when(albumFilterList.iterator()).thenReturn(albumFilterListImpl.iterator());
        albumService.getAllFilteredAlbums(albumFilterDto);
        verify(userContext, times(1)).getUserId();
        verify(albumRepository, times(1)).findAll();
        verify(albumValidator).isVisibleToRequester(requesterId, album);
    }

    @Test
    @DisplayName("testing getUserFavoriteAlbums method")
    void testGetUserFavoriteAlbums() {
        when(albumRepository.findFavoriteAlbumsByUserId(requesterId)).thenReturn(Stream.of(album));
        albumService.getUserFavoriteAlbums(albumFilterDto);
        verify(userContext, times(1)).getUserId();
        verify(albumRepository, times(1)).findFavoriteAlbumsByUserId(requesterId);
        verify(albumValidator).isVisibleToRequester(requesterId, album);
    }

    @Test
    @DisplayName("testing updateAlbum method")
    void testUpdateAlbum() {
        when(entityHandler.getOrThrowException(eq(Album.class), eq(albumId), any())).thenReturn(album);

        albumService.updateAlbum(albumId, albumDto);
        verify(userContext, times(1)).getUserId();
        verify(entityHandler, times(1)).getOrThrowException(eq(Album.class), eq(albumId), any());
        verify(albumValidator, times(1)).validateAlbumBelongsToRequester(requesterId, album);
        verify(albumRepository, times(1)).save(album);verify(albumMapper, times(1)).toDto(album);
    }

    @Test
    @DisplayName("testing deleteAlbum method")
    void testDeleteAlbum() {
        when(entityHandler.getOrThrowException(eq(Album.class), eq(albumId), any())).thenReturn(album);

        albumService.deleteAlbum(albumId);
        verify(userContext, times(1)).getUserId();

        verify(entityHandler, times(1)).getOrThrowException(eq(Album.class), eq(albumId), any());
        verify(albumValidator, times(1)).validateAlbumBelongsToRequester(requesterId, album);
        verify(albumRepository, times(1)).delete(album);
    }
}