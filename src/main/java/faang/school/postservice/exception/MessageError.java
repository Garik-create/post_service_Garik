package faang.school.postservice.exception;

public enum MessageError {

    ENTITY_NOT_FOUND_EXCEPTION("Entity %s with ID was not found."),
    FEIGN_CLIENT_UNEXPECTED_EXCEPTION("Error occurred while communicating with external service. %s"),
    USER_UNAUTHORIZED_EXCEPTION("User with ID %s is not authorized or does not exist.");

    private final String message;

    MessageError(String message) {
        this.message = message;
    }

    public String getMessage(Object... args) {
        return String.format(message, args);
    }
}
