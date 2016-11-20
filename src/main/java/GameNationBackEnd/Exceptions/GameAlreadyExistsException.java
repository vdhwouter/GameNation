package GameNationBackEnd.Exceptions;

/**
 * Created by tijs on 20/11/2016.
 */
public class GameAlreadyExistsException extends RuntimeException {
    public GameAlreadyExistsException(String gameId) {
        super("game '" + gameId + "already exists'");
    }
}
