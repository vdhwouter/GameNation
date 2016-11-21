package GameNationBackEnd.Exceptions;

/**
 * Created by tijs on 20/11/2016.
 */
public class UserAlreadyExistsException extends RuntimeException {
    public UserAlreadyExistsException(String userId) {
        super("user '" + userId + " already exists'");
    }
}
