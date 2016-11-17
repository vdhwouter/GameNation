package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin(origins = "*")
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
    @RequestMapping(method = RequestMethod.POST)
    public Game InsertGame(@RequestParam String name, @RequestParam String description, @RequestParam String imageName) {
        return gameDB.save(new Game(name, description, imageName));
    }

    // get single game by id
    @RequestMapping(value = "/{game}", method = RequestMethod.GET)
    public Game GetGame(@PathVariable Game game) {
        return game;
    }
}
