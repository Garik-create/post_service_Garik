package faang.school.postservice.dto.album;

import faang.school.postservice.enums.VisibilityAlbums;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDto {

    private long id;

    @NotBlank
    @Size(min = 1, max = 256)
    private String title;

    @NotBlank
    @Size(min = 1, max = 4096)
    private String description;

    @NotBlank
    private long authorId;

    private List<Long> postIds;

    @NotBlank
    private VisibilityAlbums visibility;

    private List<Long> beholdersIds;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}