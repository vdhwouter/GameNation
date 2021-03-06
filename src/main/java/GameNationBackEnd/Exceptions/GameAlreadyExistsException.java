package GameNationBackEnd.Exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by tijs on 20/11/2016.
 */
@ResponseStatus(HttpStatus.CONFLICT)
public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String gameId) {
        super("game '" + gameId + "' already exists");
    }
}
