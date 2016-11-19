package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UsersController {

    @Autowired
    private UserRepository userDB;

    @Autowired
    private UserGameRepository userGameDB;

    // Get all users from database (Currently for testing purposes only)
    @RequestMapping(method = RequestMethod.GET)
    public List<User> GetAllUsers() {
        return userDB.findAll();
    }

    // Get all info from database for one specified user (returned as user object)
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User GetUser(@PathVariable User user) {
        return user;
    }


    // Update all info for one specified user (returned as user object)
    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public User UpdateUser(@PathVariable User user, @RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {
        User userToEdit = user;
        // Test if the new values aren't empty to prevent loss of data
        if (!username.isEmpty()) { userToEdit.setUsername(username); }
        if (!firstname.isEmpty()) { userToEdit.setFirstname(firstname); }
        if (!lastname.isEmpty()) { userToEdit.setLastname(lastname); }
        if (!teamspeak.isEmpty()) { userToEdit.setTeamspeak(teamspeak); }
        if (!discord.isEmpty()) { userToEdit.setDiscord(discord); }
        if (!description.isEmpty()) { userToEdit.setDescription(description); }

        // Save edited user
        userDB.save(userToEdit);

        // Return user object for testing purposes
        return userToEdit;
    }

    // Save one user to database (used in registration). Object is returned for testing purposes
    @RequestMapping(method = RequestMethod.POST)
    public User InsertUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {

        // Test if username is already in use
        if (userDB.findByUsername(username) == null) {
            userDB.save(new User(username, email, password));
        } else {
            // TODO: FOUTMELDING RETURNEN DAT GEBRUIKERNAAM AL BESATAAT
        }

        // Return object for testing purposes
        return userDB.findByUsername(username);
    }

    @RequestMapping(value="/{user}/games", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable User user) {
        return userGameDB.findByUser(user);
    }

    @RequestMapping(value="/{user}/games", method = RequestMethod.POST)
    public List<UserGame> AddGameToUser(@PathVariable User user, @RequestParam Game game, @RequestParam(required = false) Integer skill) {
        UserGame newUserGame = new UserGame();

        //als skill niet ingevuld is moet deze een standaard waarde krijgen.
        if (skill == null) skill = new Integer(0);


        // TODO (@TIJS):  IMPLEMENTEER COMPARE METHOD voor game zoda ge rechtstreeks game compare kunt doen
        if (!userGameDB.findByUser(user).stream().anyMatch(ug -> ug.getGame().getId().equals(game.getId()))) {
            newUserGame.setUser(user);
            newUserGame.setGame(game);
            newUserGame.setSkill_level(skill);
            userGameDB.save(newUserGame);

        } else {
            // TODO (@TIJS, @KJELL):THROW ERROR ALS DE GAME AL BESTAAT
        }

        return userGameDB.findByUser(user);
    }
}
