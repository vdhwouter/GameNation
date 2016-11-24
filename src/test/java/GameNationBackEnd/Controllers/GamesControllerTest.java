package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import GameNationBackEnd.Setup.BaseControllerTest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lucas on 23/11/2016.
 */

public class GamesControllerTest extends BaseControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    private List<Game> gameList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    @Before
    public void setup() throws Exception {

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
    public void getGames() throws Exception {
        Game firstGame = this.gameList.get(0);

        mockMvc.perform(get("/api/games"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(this.gameList.size())))
                .andExpect(jsonPath("$[0].id", is(firstGame.getId())))
                .andExpect(jsonPath("$[0].name", is(firstGame.getName())))
                .andExpect(jsonPath("$[0].description", is(firstGame.getDescription())))
                .andExpect(jsonPath("$[0].imageName", is(firstGame.getImageName())));
    }

    @Test
    public void addGame() throws Exception {
        Game newGame = new Game("harry potter 2", "harry is er weer", "harry.jpg");
        int startLength = this.gameRepository.findAll().size();

        mockMvc.perform(post("/api/games")
                .header("Authorization", "Bearer supertoken")
                .contentType(contentType)
                .content(json(newGame)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name", is(newGame.getName())))
                .andExpect(jsonPath("$.description", is(newGame.getDescription())))
                .andExpect(jsonPath("$.imageName", is(newGame.getImageName())));

        assertEquals(startLength + 1, this.gameRepository.findAll().size());
    }

    @Test
    public void deleteGame() throws Exception {
        Random random = new Random();
        int startLength = this.gameRepository.findAll().size();
        Game game = this.gameList.get(random.nextInt(startLength-1));


        mockMvc.perform(delete("/api/games/" + game.getId())
                .header("Authorization", "Bearer supertoken"))
                .andExpect(status().is2xxSuccessful());

        assertEquals(startLength - 1, this.gameRepository.findAll().size());
        assertEquals(false, this.gameRepository.exists(game.getId()));
    }

    @Test
    public void getGame() throws Exception {
        Random random = new Random();
        int startLength = this.gameRepository.findAll().size();
        Game game = this.gameList.get(random.nextInt(startLength-1));

        mockMvc.perform(get("/api/games/" + game.getId()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.id", is(game.getId())))
                .andExpect(jsonPath("$.name", is(game.getName())))
                .andExpect(jsonPath("$.description", is(game.getDescription())))
                .andExpect(jsonPath("$.imageName", is(game.getImageName())));
    }


    /* tests te schrijven:
        - game wijzigen!
        - game toevoegen met naam die al bestaat
        - game verwijderen die niet bestaat?
        - ...
    */
}
