package user.com.user.exceptions;

public class UserAuthorizationException extends RuntimeException {
    public UserAuthorizationException(Object message) {
        super((String) message);
    }
}
