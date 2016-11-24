package GameNationBackEnd.Controllers;


import GameNationBackEnd.Controllers.UsersController;
import GameNationBackEnd.Documents.*;
import GameNationBackEnd.Repositories.*;
import GameNationBackEnd.Setup.BaseControllerTest;
import org.codehaus.groovy.ast.expr.Expression;
import org.codehaus.jackson.map.deser.ValueInstantiators;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

/**
 * Created by lucas on 23/11/2016.
 */
public class UsersControllerTest extends BaseControllerTest {

    private List<Game> gameList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserGameRepository userGameRepository;


    @Before
    public void setup() throws Exception {
        this.userGameRepository.deleteAll();
        this.userRepository.deleteAll();
        this.gameRepository.deleteAll();

        // 5 users
        this.userList.add(userRepository.save(new User("lucas", "lucas@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("kjell", "kjell@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("tijs", "tijs@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("matthias", "matthias@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("wouter", "wouter@gmail.com", "Azerty123")));

        // 7 games
        this.gameList.add(gameRepository.save(new Game("super mario", "race your friends", "mario.jpg")));
        this.gameList.add(gameRepository.save(new Game("counter-strike", "kill them instead", "cs.jpg")));
        this.gameList.add(gameRepository.save(new Game("payday", "shooting i guess", "payday.jpg")));
        this.gameList.add(gameRepository.save(new Game("windows movie maker", "not a game", "wmm.jpg")));
        this.gameList.add(gameRepository.save(new Game("civilization 6", "conquer", "civ.jpg")));
        this.gameList.add(gameRepository.save(new Game("dota 2", "free they say", "dota.jpg")));
        this.gameList.add(gameRepository.save(new Game("team fortress 2", "still popular", "wmm.jpg")));
    }

    @Test
    public void getUsers() throws Exception {
        User firstUser = this.userList.get(0);

        mockMvc.perform(get("/api/users"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(contentType))
            .andExpect(jsonPath("$", hasSize(this.userList.size())))
            .andExpect(jsonPath("$[0].id", is(firstUser.getId())))
            .andExpect(jsonPath("$[0].username", is(firstUser.getUsername())))
            .andExpect(jsonPath("$[0].email", is(firstUser.getEmail())));
    }


    @Test
    public void getUserByUsername() throws Exception {
        User firstUser = this.userList.get(0);

        mockMvc.perform(get("/api/users?username=" + firstUser.getUsername()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(firstUser.getId())))
                .andExpect(jsonPath("$[0].username", is(firstUser.getUsername())))
                .andExpect(jsonPath("$[0].email", is(firstUser.getEmail())));
    }

    @Test
    public void addUser() throws Exception {
        String userJson = json(new User("boeferke", "boefer@gmail.com", "bloempje123"));
        int startLength = this.userRepository.findAll().size();

        mockMvc.perform(post("/api/users")
                .header("Authorization", "Bearer supertoken")
                .contentType(contentType)
                .content(userJson))
                .andExpect(status().isCreated());

        assertEquals(startLength + 1, this.userRepository.findAll().size());
    }

    @Test
    public void deleteUser() throws Exception {
        Random random = new Random();
        int startLength = this.userRepository.findAll().size();
        User user = this.userList.get(random.nextInt(startLength-1));


        mockMvc.perform(delete("/api/users/" + user.getId())
                .header("Authorization", "Bearer supertoken"))
                .andExpect(status().is2xxSuccessful());

        assertEquals(startLength - 1, this.userRepository.findAll().size());
        assertEquals(false, this.userRepository.exists(user.getId()));
    }

    @Test
    public void getUser() throws Exception {
        Random random = new Random();
        int startLength = this.userRepository.findAll().size();
        User user = this.userList.get(random.nextInt(startLength-1));


        mockMvc.perform(get("/api/users/" + user.getId())
                .header("Authorization", "Bearer supertoken"))
                .andExpect(content().contentType(contentType))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    @Test
    public void updateUser() throws Exception {
        Random random = new Random();
        int startLength = this.userRepository.findAll().size();
        String userId = this.userList.get(random.nextInt(startLength-1)).getId();
        User oldUser = this.userRepository.findOne(userId);
        User updatingValueUser = this.userRepository.findOne(userId);

        String newUsername = "test usernameke super yeey";
        updatingValueUser.setUsername(newUsername);

        mockMvc.perform(post("/api/users/" + userId)
                .header("Authorization", "Bearer supertoken")
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(newUsername)));

        User updatedUser = userRepository.findOne(oldUser.getId());
        assertNotEquals(oldUser.getUsername(), updatedUser.getUsername());
        assertNotNull(this.userRepository.findByUsername(newUsername));
    }

    @Test
    public void addGamesToUser() throws Exception {
        User user = userRepository.findByUsername("wouter");

        Game game1 = this.gameList.get(2);
        Game game2 = this.gameList.get(4);

        List<String> gameIds = Arrays.asList(game1.getId(), game2.getId());

        mockMvc.perform(post("/api/users/" + user.getId() + "/games")
                .header("Authorization", "Bearer supertoken")
                .contentType(contentType)
                .content(json(gameIds)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].game.name", is(game1.getName())))
                .andExpect(jsonPath("$[1].game.name", is(game2.getName())));

        List<UserGame> userGames = userGameRepository.findByUser(user);
        assertEquals(2, userGames.size());
    }


    @Test
    public void getGamesForUser() throws Exception {
        User user = userRepository.findByUsername("wouter");

        Game game1 = this.gameList.get(2);
        Game game2 = this.gameList.get(4);
        Game game3 = this.gameList.get(6);

        userGameRepository.save(new UserGame(user, game1, 0));
        userGameRepository.save(new UserGame(user, game2, 0));

        UserGame ug = new UserGame();
        ug.setGame(game3);
        ug.setUser(user);
        ug.setSkill_level(0);
        userGameRepository.save(ug);

        mockMvc.perform(get("/api/users/" + user.getId() + "/games")
                .header("Authorization", "Bearer supertoken")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andExpect(jsonPath("$[0].game.name", is(game1.getName())))
                .andExpect(jsonPath("$[1].game.name", is(game2.getName())));

        List<UserGame> userGames = userGameRepository.findByUser(user);
        assertEquals(3, userGames.size());
    }

    @Test
    public void EmailValidationTest() throws Exception {
        User user = new User("borrel", "bale_at_gool.com", "superwachtwoord");

        mockMvc.perform(post("/api/users")
            .header("Authorization", "Bearer supertoken")
            .contentType(contentType)
            .content(json(user)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.errors", hasKey("email")))
            .andExpect(jsonPath("$.errors.email", is("Email must be a valid email")));
    }


    /* tests te schrijven:
        - user wijzigen!
        - user toevoegen met naam die al bestaat
        - user verwijderen die niet bestaat?
        - game toevoegen aan user die niet bestaat
        - game verwijderen van user die niet bestaat
        - game verwijderen die niet bestaat van user
        - ...
    */
}
