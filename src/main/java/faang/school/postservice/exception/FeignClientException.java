package faang.school.postservice.exception;

public class FeignClientException extends RuntimeException {

    public FeignClientException(String message) {
        super(message);
    }

    public FeignClientException(String message, Exception e) {
        super(message, e);
    }
}