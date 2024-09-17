package faang.school.postservice.repository;

import faang.school.postservice.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

@Repository
public interface AlbumRepository extends JpaRepository<Album, Long> {

    boolean existsByTitleAndAuthorId(String title, long authorId);

    Stream<Album> findByAuthorId(long authorId);

    @Query("SELECT a FROM Album a LEFT JOIN FETCH a.posts WHERE a.id = :id")
    Optional<Album> findByIdWithPosts(long id);

    @Query(nativeQuery = true, value = "INSERT INTO favorite_albums (album_id, user_id) VALUES (:albumId, :userId)")
    @Modifying
    void addAlbumToFavorites(long albumId, long userId);

    @Query(nativeQuery = true, value = "DELETE FROM favorite_albums WHERE album_id = :albumId AND user_id = :userId")
    @Modifying
    void deleteAlbumFromFavorites(long albumId, long userId);

    @Query(nativeQuery = true, value = """
            SELECT a.* FROM album a
            WHERE id IN (
                SELECT album_id FROM favorite_albums WHERE user_id = :userId
            )
            """)
    Stream<Album> findFavoriteAlbumsByUserId(long userId);

    List<Album> findByTitleContainingIgnoreCase(String pattern);

    List<Album> findByCreatedAtAfter(LocalDateTime after);

    List<Album> findByCreatedAtBefore(LocalDateTime before);

    @Query(nativeQuery = true, value = """
            SELECT a.* FROM album a
            INNER JOIN favorite_albums fa
            ON a.id = fa.album_id
            WHERE a.id IN :ids
            """)
    List<Album> findFavoriteAlbumByAllIds(List<Long> ids);
}
