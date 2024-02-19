package faang.school.postservice.moderation;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@Slf4j
public class ModerationDictionary {
    private Set<String> badWords;

    @PostConstruct
    private void collectionBadWords() {
        Path filePath = Path.of("src/main/resources/dictionary.txt");
        try (Stream<String> lines = Files.lines(filePath)) {
            badWords = lines
                    .map(String::trim)
                    .map(String::toLowerCase)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            log.error("Dictionary not created");
            throw new RuntimeException(e);
        }
    }

    public boolean containsBadWord(String string) {
        String lowerCaseText = string.toLowerCase();
        return badWords.stream().anyMatch(lowerCaseText::contains);
    }
}
