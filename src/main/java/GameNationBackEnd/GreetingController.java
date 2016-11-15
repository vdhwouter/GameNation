package GameNationBackEnd;

import java.util.*;

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
	public User InsertUser(@RequestParam String username, @RequestParam String email, @RequestParam String password) {
		userDB.save(new User(username, email, password));
		return userDB.findByUsername(username);
	}
}