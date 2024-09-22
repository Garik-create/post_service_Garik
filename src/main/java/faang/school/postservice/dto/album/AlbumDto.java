package faang.school.postservice.dto.album;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlbumDto {
    @NotBlank
    private String title;
    @NotBlank
    private String description;
    private Long authorId;
    private List<Long> postIds;
}
