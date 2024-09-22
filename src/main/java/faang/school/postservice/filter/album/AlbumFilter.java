package faang.school.postservice.filter.album;

import faang.school.postservice.dto.album.filter.AlbumFilterDto;
import faang.school.postservice.model.Album;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public interface AlbumFilter {
    boolean isApplicable(AlbumFilterDto albumFilterDto);

    Stream<Album> apply(Stream<Album> albumStream, AlbumFilterDto albumFilterDto);
}
