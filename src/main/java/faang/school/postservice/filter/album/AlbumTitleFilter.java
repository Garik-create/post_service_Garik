package faang.school.postservice.filter.album;

import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.model.Album;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AlbumTitleFilter implements AlbumFilter {

    @Override
    public boolean isApplicable(AlbumFilterDto filterDto) {
        String titlePattern = filterDto.getTitlePattern();
        return titlePattern != null && !titlePattern.isBlank();
    }

    @Override
    public Stream<Album> apply(Stream<Album> albums, AlbumFilterDto filterDto) {
        String titlePattern = filterDto.getTitlePattern().toLowerCase();
        return albums.filter(album -> album.getTitle().toLowerCase().contains(titlePattern));
    }
}