package faang.school.postservice.client.external.spelling;

import faang.school.postservice.dto.spelling_corrector.text_gears.TextGearsBodyResponse;
import faang.school.postservice.dto.spelling_corrector.text_gears.TextGearsCorrectResponse;
import faang.school.postservice.dto.spelling_corrector.text_gears.TextGearsLang;
import faang.school.postservice.dto.spelling_corrector.text_gears.TextGearsLangDetectResponse;
import faang.school.postservice.exception.spelling_corrector.DontRepeatableServiceException;
import faang.school.postservice.exception.spelling_corrector.RepeatableServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Slf4j
@Component
@RequiredArgsConstructor
public class TextGearsClient {
    private final static String CORRECTOR_ENDPOINT = "/correct";
    private final static String LANG_DETECTOR_ENDPOINT = "/detect";
    private final RestTemplate restTemplate;

    @Value("${post.spelling-corrector.client.textgears.auth-token}")
    private String authToken;

    @Value("${post.spelling-corrector.client.textgears.host}")
    private String serviceHost;

    @Retryable(retryFor = {RepeatableServiceException.class}, backoff = @Backoff(delay = 2000, multiplier = 2))
    public String correctText(String text) {
        String url = serviceHost + CORRECTOR_ENDPOINT;
        URI uri = makeUri(text, url);

        ResponseEntity<TextGearsCorrectResponse> responseEntity = restTemplate.getForEntity(
                uri, TextGearsCorrectResponse.class);

        TextGearsCorrectResponse response = checkAndGetResponse(responseEntity);

        return response.getResponse().getCorrected();
    }

    @Retryable(retryFor = {RepeatableServiceException.class}, backoff = @Backoff(delay = 2000, multiplier = 2))
    public TextGearsLang detectLang(String text) {
        String url = serviceHost + LANG_DETECTOR_ENDPOINT;
        URI uri = makeUri(text, url);

        ResponseEntity<TextGearsLangDetectResponse> responseEntity = restTemplate.getForEntity(
                uri, TextGearsLangDetectResponse.class);
        TextGearsLangDetectResponse response = checkAndGetResponse(responseEntity);

        TextGearsLang lang = TextGearsLang.fromString(response.getResponse().getLanguage());

        return lang;
    }

    private URI makeUri(String text, String url) {
        return UriComponentsBuilder.fromHttpUrl(url)
                .queryParam("text", text)
                .queryParam("key", authToken)
                .build()
                .encode()
                .toUri();
    }

    private <T extends TextGearsBodyResponse> T checkAndGetResponse(ResponseEntity<T> responseEntity) {
        int statusCode = responseEntity.getStatusCode().value();
        T response = responseEntity.getBody();

        if (statusCode > 500) {
            log.error("Ошибка при получении корректировки от TextGears {}", responseEntity);

            throw new RepeatableServiceException();
        }

        if (response == null) {
            log.warn("От сервиса {} пришёл пустой ответ", serviceHost);

            throw new RepeatableServiceException();
        }

        if (!response.isStatus()) {
            log.error("Сервис корректировки вернул ошибку {}", responseEntity);

            throw new DontRepeatableServiceException();
        }

        return response;
    }
}