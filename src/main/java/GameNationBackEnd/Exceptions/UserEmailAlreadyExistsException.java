package GameNationBackEnd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tijs on 29/11/2016.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class UserEmailAlreadyExistsException extends RuntimeException{
    public UserEmailAlreadyExistsException(String email) {
        super("email '" + email + "' already exists for another user");
    }
}
