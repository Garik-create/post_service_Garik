package faang.school.postservice.dto.comment;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEventDto {
    private Long commentId;
    private Long commentAuthorId;
    private Long postId;
    private Long postAuthorId;
    private String commentContent;

    @Override
    public String toString() {
        return "{" +
               "\"commentId\":" + commentId + "," +
               "\"commentAuthorId\":" + commentAuthorId + "," +
               "\"postId\":" + postId + "," +
               "\"postAuthorId\":" + postAuthorId + "," +
               "\"commentContent\":\"" + commentContent + "\"" +
               "}";
    }
}
