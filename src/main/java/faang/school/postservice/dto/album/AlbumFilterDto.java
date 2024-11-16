package faang.school.postservice.dto.album;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AlbumFilterDto {

    private String titlePattern;
    private String descriptionPattern;
    private LocalDateTime createdBefore;
    private LocalDateTime createdAfter;
}