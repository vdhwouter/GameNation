package GameNationBackEnd.Exceptions;

/**
 * Created by lucas on 19/11/2016.
 */
public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String userId) {
        super("could not find user '" + userId + "'.");
    }
}
