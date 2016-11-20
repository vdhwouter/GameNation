package GameNationBackEnd.Exceptions;

/**
 * Created by tijs on 20/11/2016.
 */
public class GameNotFoundException extends RuntimeException {
    public GameNotFoundException(String gameId) {
        super("could not find game '" + gameId + "'.");
    }
}
