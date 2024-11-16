package faang.school.postservice.filter.album;

import faang.school.postservice.dto.album.AlbumFilterDto;
import faang.school.postservice.model.Album;
import org.springframework.stereotype.Component;

import java.util.stream.Stream;

@Component
public class AlbumDescriptionFilter implements AlbumFilter {

    @Override
    public boolean isApplicable(AlbumFilterDto filterDto) {
        String descriptionPattern = filterDto.getDescriptionPattern();
        return descriptionPattern != null && !descriptionPattern.isBlank();
    }

    @Override
    public Stream<Album> apply(Stream<Album> albums, AlbumFilterDto filterDto) {
        String descriptionPattern = filterDto.getDescriptionPattern().toLowerCase();
        return albums.filter(album -> album.getDescription().toLowerCase().contains(descriptionPattern));
    }
}