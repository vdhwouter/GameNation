package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Repositories.GameRepository;
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

    // get all games
    @RequestMapping(method = RequestMethod.GET)
    public List<Game> GetAllGames() {
        return gameDB.findAll();
    }

// add a game
//    @RequestMapping(method = RequestMethod.POST)
//    public Game InsertGame(@RequestParam String name, @RequestParam String description, @RequestParam String imageName) {
//        return gameDB.save(new Game(name, description, imageName));
//    }

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

    @RequestMapping(value = "/{game}", method = RequestMethod.DELETE)
    public void DeleteGame(@PathVariable Game game) {
        gameDB.delete(game);
    }
}
