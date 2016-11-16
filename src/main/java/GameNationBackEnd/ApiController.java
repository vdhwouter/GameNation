package GameNationBackEnd;

import java.util.*;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

@RestController
@RequestMapping("/api")
public class ApiController {

	// MongoDB Connection
	@Autowired
	private UserRepository userDB;

	@Autowired
	private GameRepository gameDB;

	// Get all users from database (Currently for testing purposes only)
	// CrossOrigin annotion must be added to prevent cross site errors
	@CrossOrigin
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> GetAllUsers() {
		return userDB.findAll();
	}


	// Get all info from database for one specified user (returned as user object)
	@CrossOrigin
	@RequestMapping(value = "/users/{user}", method = RequestMethod.GET)
	public User GetUser(@PathVariable User user) {
//		return userDB.findByUsername(username);
        return user;
    }

	// Save one user to database (used in registration). Object is returned for testing purposes
	@CrossOrigin
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User InsertUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {

		// Get user that needs to be edited
		User userToEdit = userDB.findByUsername(username);

		if (userToEdit == null) {
			userDB.save(new User(username, email, password));
		} else {
			// Edit all fields independently to prevent data loss
			if (!username.isEmpty()) {
				userToEdit.setUsername(username);
			}
			if (!email.isEmpty()) {
				userToEdit.setEmail(email);
			}
			if (!password.isEmpty()) {
				userToEdit.setPassword(password);
			}
			if (!firstname.isEmpty()) {
				userToEdit.setFirstname(firstname);
			}
			if (!lastname.isEmpty()) {
				userToEdit.setLastname(lastname);
			}
			if (!teamspeak.isEmpty()) {
				userToEdit.setTeamspeak(teamspeak);
			}
			if (!discord.isEmpty()) {
				userToEdit.setDiscord(discord);
			}
			if (!description.isEmpty()) {
				userToEdit.setDescription(description);
			}

			// Save edited user
			userDB.save(userToEdit);
		}

		// Return object for testing purposes
		return userDB.findByUsername(username);
	}

	@CrossOrigin
	@RequestMapping(value = "/games", method = RequestMethod.POST)
	public Game InsertGame(@RequestParam String name, @RequestParam String description, @RequestParam String imageName) {
		gameDB.save(new Game(name, description, imageName));
		return gameDB.findByName(name);
	}

	@CrossOrigin
	@RequestMapping(value = "/games", method = RequestMethod.GET)
	public List<Game> GetAllGames() {
		return gameDB.findAll();
	}

	@CrossOrigin
	@RequestMapping(value = "/games/{name}", method = RequestMethod.GET)
	public Game GetGame(@PathVariable String name) {
		return gameDB.findByName(name);
	}
}