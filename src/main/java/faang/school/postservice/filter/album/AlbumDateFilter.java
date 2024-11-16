package faang.school.postservice.filter.album;

import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.model.Album;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.stream.Stream;

@Component
public class AlbumDateFilter implements AlbumFilter {

    @Override
    public boolean isApplicable(AlbumFilterDto filterDto) {
        LocalDateTime createdBefore = filterDto.getCreatedBefore();
        LocalDateTime createdAfter = filterDto.getCreatedAfter();

        if (createdAfter == null && createdBefore == null) {
            return false;
        }
        if (createdAfter != null && createdBefore != null) {
            return createdBefore.isAfter(createdAfter);
        }
        return true;
    }

    @Override
    public Stream<Album> apply(Stream<Album> albums, AlbumFilterDto filterDto) {
        LocalDateTime createdBefore = filterDto.getCreatedBefore();
        LocalDateTime createdAfter = filterDto.getCreatedAfter();

        if (createdBefore != null) {
            albums = albums.filter(album -> album.getCreatedAt().isBefore(createdBefore));
        }
        if (createdAfter != null) {
            albums = albums.filter(album -> album.getCreatedAt().isAfter(createdAfter));
        }

        return albums;
    }
}