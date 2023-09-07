package faang.school.postservice.dto.redis;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CommentEventDto {
    private long idComment;
    private String contentComment;
    private long authorIdComment;
    private Long postId;
    private Long postAuthorId;
    private LocalDateTime createdAt;
}