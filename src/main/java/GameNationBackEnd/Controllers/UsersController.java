package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.RequestDocuments.SkillLevelRequest;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.aggregation.Fields;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

import GameNationBackEnd.Exceptions.*;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsersController {


    @Autowired
    private UserRepository userDB;

    @Autowired
    private UserGameRepository userGameDB;

    @Autowired
    private GameRepository gameRepository;

    // get all users
    @RequestMapping(method = RequestMethod.GET)
    public List<User> GetUsers() {
        return userDB.findAll();
    }

    // get user by username
    @RequestMapping(method = RequestMethod.GET, params = {"username"})
    public List<User> GetUserByUsername(@RequestParam String username) {
        User user = userDB.findByUsername(username);
        if (user != null) {
            return Arrays.asList(userDB.findByUsername(username));
        } else {
            return new ArrayList<>();
        }
    }

    // get user by id
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User GetUser(@PathVariable User user) {
        return user;
    }

    // update user
    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public User UpdateUser(@PathVariable User user, @RequestBody User updatedUser) {

        if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        if (updatedUser.getFirstname() != null) user.setFirstname(updatedUser.getFirstname());
        if (updatedUser.getLastname() != null) user.setLastname(updatedUser.getLastname());
        if (updatedUser.getTeamspeak() != null) user.setTeamspeak(updatedUser.getTeamspeak());
        if (updatedUser.getDiscord() != null) user.setDiscord(updatedUser.getDiscord());
        if (updatedUser.getDescription() != null) user.setDescription(updatedUser.getDescription());

        if (userDB.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        } else {
            userDB.delete(user);
            userDB.save(user);
        }

        return user;
    }

    // create a user
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User InsertUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        if (userDB.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        } else {
            return userDB.save(user);
        }
    }

    // delete user
    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void DeleteUser(@PathVariable User user) {
        userDB.delete(user);
    }

    // get games for user
    @RequestMapping(value = "/{user}/games", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable User user) {
        return userGameDB.findByUser(user);
    }

    // add games to user
    @RequestMapping(value = "/{user}/games", method = RequestMethod.POST)
    public List<UserGame> AddGamesToUser(@PathVariable User user, @RequestBody List<String> games) {
        List<Game> gameList = gameRepository.findByIdIn(games);

        // check if any of the games already exist in the user's collection
        userGameDB.findByUser(user).stream().map(ug -> ug.getGame()).forEach(game -> {
            gameList.forEach(g -> {
                if (g.equals(game)) {
                    throw new GameAlreadyExistsException(game.getName());
                }
            });
        });

        for (Game game : gameList) {
            userGameDB.save(new UserGame(user,game, 0));
        }

        return userGameDB.findByUser(user);
    }

    // set skill on game for user
    @RequestMapping(value = "/{user}/games/{game}", method = RequestMethod.POST)
    public UserGame setSkillLevelForUserGame(@PathVariable User user, @PathVariable Game game, @RequestBody SkillLevelRequest skillLevel) {
        UserGame ug = userGameDB.findByUserAndGame(user, game);
        ug.setSkill_level(skillLevel.level);
        return userGameDB.save(ug);
    }

    // delete game from user
    @RequestMapping(value = "/{user}/games/{game}", method = RequestMethod.DELETE)
    public void deleteGameFromUser(@PathVariable User user, @PathVariable Game game) {
        UserGame ug = userGameDB.findByUserAndGame(user, game);
        userGameDB.delete(ug);
    }

    // get logged in user
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User GetAuthenticatedUser(Principal principal) {
        // principal contains authenticated user name
        User user = userDB.findByUsername(principal.getName());
        return user;
    }
}
