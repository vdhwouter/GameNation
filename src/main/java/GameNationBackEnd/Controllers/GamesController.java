package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserGameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/games")
public class GamesController {

    @Autowired
    private GameRepository gameDB;

    @Autowired
    private UserGameRepository userGameDB;

    // get all games
    @RequestMapping(method = RequestMethod.GET)
    public List<Game> GetAllGames() {
        return gameDB.findAll();
    }

    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public Game InsertGame(@Valid @RequestBody Game game) {
        return gameDB.save(game);
    }

    // get single game by id
    @RequestMapping(value = "/{game}", method = RequestMethod.GET)
    public Game GetGame(@PathVariable Game game) {
        return game;
    }


    // get users for game
    @RequestMapping(value = "/{game}/users", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable Game game) {
        return userGameDB.findByGame(game);
    }

    @RequestMapping(value = "/{game}", method = RequestMethod.DELETE)
    public void DeleteGame(@PathVariable Game game) {
        gameDB.delete(game);
    }

}
