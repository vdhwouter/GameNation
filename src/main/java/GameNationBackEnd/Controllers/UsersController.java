package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import java.io.*;
import java.security.Principal;
import java.util.*;
import GameNationBackEnd.Exceptions.*;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsersController{


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
        return Arrays.asList(userDB.findByUsername(username));
    }

    // get user by id
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User GetUser(@PathVariable User user) {
        return user;
    }

    // Update all info for one specified user (returned as user object)
//    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
//    public User UpdateUser(@PathVariable User user, @RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {
//
//        // Test if the new values aren't empty to prevent loss of data
//        if (!username.isEmpty()) { user.setUsername(username); }
//        if (!firstname.isEmpty()) { user.setFirstname(firstname); }
//        if (!lastname.isEmpty()) { user.setLastname(lastname); }
//        if (!teamspeak.isEmpty()) { user.setTeamspeak(teamspeak); }
//        if (!discord.isEmpty()) { user.setDiscord(discord); }
//        if (!description.isEmpty()) { user.setDescription(description); }
//
//        // Save edited user
//        userDB.save(user);
//
//        // Return user object for testing purposes
//        return user;
//    }
    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public User UpdateUser(@PathVariable User user, @RequestBody User updatedUser) {
        // either this
        if (updatedUser.getUsername() != null) { user.setUsername(updatedUser.getUsername()); };
        if (updatedUser.getEmail() != null) { user.setEmail(updatedUser.getEmail()); };
        if (updatedUser.getPassword() != null) { user.setPassword(updatedUser.getPassword()); };
        if (updatedUser.getFirstname() != null) { user.setFirstname(updatedUser.getFirstname()); };
        if (updatedUser.getLastname() != null) { user.setLastname(updatedUser.getLastname()); };
        if (updatedUser.getTeamspeak() != null) { user.setTeamspeak(updatedUser.getTeamspeak()); };
        if (updatedUser.getDiscord() != null) { user.setDiscord(updatedUser.getDiscord()); };
        if (updatedUser.getDescription() != null) { user.setDescription(updatedUser.getDescription()); };

        // or this , with @Valid @RequestBody User updatedUser
//        updatedUser.setId(user.getId());

        return userDB.save(updatedUser);
    }

    // create a user
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User InsertUser(@Valid @RequestBody User user) throws UserAlreadyExistsException {
        if (userDB.findByUsername(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getId());
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
    @RequestMapping(value="/{user}/games", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable User user) {
        return userGameDB.findByUser(user);
    }

    //two functions
    //1. add a list of games to a user with default skill 0
    //2. edit skill of a single game bij giving requestParam skill
    @RequestMapping(value="/{user}/games2", method = RequestMethod.POST)
    public List<UserGame> AddGameToUser(@PathVariable User user, @RequestParam List<Game> games, @RequestParam(required = false) Integer skill) throws GameAlreadyExistsException {

        //als skill niet ingevuld is moet deze een standaard waarde krijgen.
        if (skill == null) skill = new Integer(0);

        for(Game game: games){
            if (!userGameDB.findByUser(user).stream().anyMatch(ug -> ug.getGame().equals(game))) {
                UserGame newUserGame = new UserGame(user, game, skill);
                userGameDB.save(newUserGame);
            } else {
                UserGame editUserGame = userGameDB.findByUserAndGame(user,game);
                if(editUserGame.getSkill_level() != skill){
                    editUserGame.setSkill_level(skill);
                    userGameDB.save(editUserGame);
                }
                else throw new GameAlreadyExistsException(game.getId());
            }
        }

        return userGameDB.findByUser(user);
    }

    @RequestMapping(value="/{user}/games", method = RequestMethod.POST)
    public List<UserGame> AddGamesToUser(@PathVariable User user, @RequestBody List<String> games) {
        // skill moe wel hier nie peisk als ge het nie toevoegt van de eerste keer.
        // of ja alleszins altijd 0 zijn zoals.. wel hier :p
        int skill = new Integer(0);

        List<Game> gameList = gameRepository.findByIdIn(games);
        for(Game game: gameList){
            if (!userGameDB.findByUser(user).stream().anyMatch(ug -> ug.getGame().equals(game))) {
                UserGame newUserGame = new UserGame(user, game, skill);
                userGameDB.save(newUserGame);
            } else {
                UserGame editUserGame = userGameDB.findByUserAndGame(user,game);
                if(editUserGame.getSkill_level() != skill){
                    editUserGame.setSkill_level(skill);
                    userGameDB.save(editUserGame);
                }
                else throw new GameAlreadyExistsException(game.getId());
            }
        }

        return userGameDB.findByUser(user);
    }

    @RequestMapping(value ="/{user}/games/{game}", method = RequestMethod.DELETE)
    public String deleteGameFromUser(@PathVariable User user, @PathVariable Game game) {
        UserGame ug = userGameDB.findByUserAndGame(user,game);
        userGameDB.delete(ug);
        return "deleted";
    }

    @RequestMapping(value="/me", method = RequestMethod.GET)
    public User GetAuthenticatedUser(Principal principal) {
        // principal contains authenticated user name
        User user = userDB.findByUsername(principal.getName());
        return user;
    }
}
