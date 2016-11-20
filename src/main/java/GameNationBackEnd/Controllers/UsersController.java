package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.ServletContext;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.Principal;
import java.util.List;
import GameNationBackEnd.Exceptions.*;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/users")
public class UsersController{

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
    public User UpdateUser(@PathVariable User user, @RequestParam(value = "image", required = false) MultipartFile image, @RequestParam(required = false) String username, @RequestParam(required = false) String email, @RequestParam(required = false) String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {

//Poging voor fileupload
//        if (!image.isEmpty()) {
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream("abc.jpg");
//                fos.write(image.getBytes());
//                fos.close();
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }


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
    public User InsertUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) throws UserAlreadyExistsException {

        // Test if username is already in use
        if (userDB.findByUsername(username) == null) {
            userDB.save(new User(username, email, password));
        } else {
            User user = userDB.findByUsername(username);
            throw new UserAlreadyExistsException(user.getId());
        }

        // Return object for testing purposes
        return userDB.findByUsername(username);
    }

    @RequestMapping(value="/{user}/games", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable User user) {
        return userGameDB.findByUser(user);
    }

    @RequestMapping(value="/{user}/games", method = RequestMethod.POST)
public List<UserGame> AddGameToUser(@PathVariable User user, @RequestParam Game game, @RequestParam(required = false) Integer skill) throws GameAlreadyExistsException {

        //als skill niet ingevuld is moet deze een standaard waarde krijgen.
        if (skill == null) skill = new Integer(0);

        if (userGameDB.findByUser(user).stream().anyMatch(ug -> ug.getGame().equals(game))) {
            UserGame newUserGame = new UserGame(user, game, skill);
            userGameDB.save(newUserGame);
        } else {
            throw new GameAlreadyExistsException(game.getId());
        }

        return userGameDB.findByUser(user);
    }

    @RequestMapping(value="/me", method = RequestMethod.GET)
    public User GetAuthenticatedUser(Principal principal) {
        // principal contains authenticated user name
        User user = userDB.findByUsername(principal.getName());
        return user;
    }
}
