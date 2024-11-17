package faang.school.postservice.model.kafka;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@AllArgsConstructor
public class KafkaCommentEvent implements Serializable {

    private long authorId;
    private long postId;
    private String content;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private AtomicLong likesAmount;

}
