package faang.school.postservice.exception;

public class UnauthorizedException extends RuntimeException {

    public UnauthorizedException(long userId, Exception e) {
        super(MessageError.USER_UNAUTHORIZED_EXCEPTION.getMessage(userId), e);
    }
}