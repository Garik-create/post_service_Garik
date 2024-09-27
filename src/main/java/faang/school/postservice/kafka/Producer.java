package faang.school.postservice.kafka;

import faang.school.postservice.dto.event.PostPublishedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class Producer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void send(String topic, PostPublishedEvent event) {
        log.info("Sending postPublishedEvent: {} to topic: {}", event, topic);
        kafkaTemplate.send(topic, event);
    }
}
