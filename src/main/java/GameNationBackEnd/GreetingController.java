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
public class GreetingController {

	// MongoDB Connection
	@Autowired
	private UserRepository userDB;

	// Get all users from database (Currently for testing purposes only)
	// CrossOrigin annotion must be added to prevent cross site errors
	@CrossOrigin
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public List<User> GetAllUsers() {
		return userDB.findAll();
	}


	// Get all info from database for one specified user (returned as user object)
	@CrossOrigin
	@RequestMapping(value = "/users/{username}", method = RequestMethod.GET)
	public User GetUser(@PathVariable String username) {
		return userDB.findByUsername(username);
	}

	// Save one user to database (used in registration). Object is returned for testing purposes
	@CrossOrigin
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public User InsertUser(@RequestParam String username, @RequestParam String email, @RequestParam String password, @RequestParam(required = false) String firstname, @RequestParam(required = false) String lastname, @RequestParam(required = false) String teamspeak, @RequestParam(required = false) String discord, @RequestParam(required = false) String description) {

		User userToEdit = userDB.findByUsername(username);

		if (!username.isEmpty()) { userToEdit.setUsername(username); }
		if (!email.isEmpty()) { userToEdit.setEmail(email); }
		if (!password.isEmpty()) { userToEdit.setPassword(password); }
		if (!firstname.isEmpty()) { userToEdit.setFirstname(firstname); }
		if (!lastname.isEmpty()) { userToEdit.setLastname(lastname); }
		if (!teamspeak.isEmpty()) { userToEdit.setTeamspeak(teamspeak); }
		if (!discord.isEmpty()) {userToEdit.setDiscord(discord); }
		if (!description.isEmpty()) { userToEdit.setDescription(description); }

		userDB.save(userToEdit);

		return userDB.findByUsername(username);
	}
}