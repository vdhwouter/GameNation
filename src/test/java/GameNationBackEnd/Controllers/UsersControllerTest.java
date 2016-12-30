package GameNationBackEnd.Controllers;


import GameNationBackEnd.Documents.*;
import GameNationBackEnd.Filters.RoutingFilter;
import GameNationBackEnd.Repositories.*;
import GameNationBackEnd.Setup.BaseControllerTest;
import GameNationBackEnd.RequestDocuments.SkillLevelRequest;
import GameNationBackEnd.Setup.UserPrincipal;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

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

    private User getRandomUser() {
        Random random = new Random();
        int startLength = this.userRepository.findAll().size();
        return this.userList.get(random.nextInt(startLength - 1));
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
    public void getUserByUsernameNotCaseSensitive() throws Exception {
        User firstUser = this.userList.get(0);

        mockMvc.perform(get("/api/users?username=" + firstUser.getUsername().toUpperCase()))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(firstUser.getId())))
                .andExpect(jsonPath("$[0].username", is(firstUser.getUsername())))
                .andExpect(jsonPath("$[0].email", is(firstUser.getEmail())));
    }

    @Test
    public void addUser() throws Exception {
//        User user = new User("boeferke", "boefer@gmail.com", "bloempje123");
//        int startLength = this.userRepository.findAll().size();
//
////        mockMvc.perform(post("/api/users")
////                .contentType(contentType)
////                .content(json(user)))
////                .andExpect(status().isCreated());
//
//        // password should be encrypted
//        assertTrue(new BCryptPasswordEncoder().matches(
//                user.getPassword(),
//                userRepository.findByUsername(user.getUsername()).getPassword()));
//
//        assertEquals(startLength + 1, this.userRepository.findAll().size());
    }

    @Test
    public void deleteUser() throws Exception {
        User user = getRandomUser();
        long startLength = this.userRepository.count();

        mockMvc.perform(delete("/api/users/" + user.getId()))
                .andExpect(status().is2xxSuccessful());

        assertEquals(startLength - 1, this.userRepository.count());
        assertEquals(false, this.userRepository.exists(user.getId()));
    }

    @Test
    public void getUser() throws Exception {
        User user = getRandomUser();

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(content().contentType(contentType))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$.id", is(user.getId())))
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }


    @Test
    public void updateUser() throws Exception {
        User oldUser = getRandomUser();
        String userId = oldUser.getId();
        User updatingValueUser = this.userRepository.findOne(userId);

        String newUsername = "test usernameke super yeey";
        updatingValueUser.setUsername(newUsername);

        mockMvc.perform(post("/api/users/" + userId)
                .principal(new UserPrincipal(oldUser))
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(newUsername)));

        User updatedUser = userRepository.findOne(oldUser.getId());
        assertNotEquals(oldUser.getUsername(), updatedUser.getUsername());
        assertNotNull(this.userRepository.findByUsername(newUsername));
    }

    @Test
    // try to update a user without being authorized
    public void updateUserUnauthorized() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        String userId = user1.getId();
        User updatingValueUser = this.userRepository.findOne(userId);

        String newUsername = "test usernameke super yeey";
        updatingValueUser.setUsername(newUsername);

        // as a logged in user
        mockMvc.perform(post("/api/users/" + userId)
                .principal(new UserPrincipal(user2))
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isForbidden());

        User updatedUser = userRepository.findOne(user1.getId());
        assertEquals(user1.getUsername(), updatedUser.getUsername());

        // without being logged in
        mockMvc.perform(post("/api/users/" + userId)
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isForbidden());

         updatedUser = userRepository.findOne(user1.getId());
        assertEquals(user1.getUsername(), updatedUser.getUsername());
    }

    @Test
    public void updateUserOtherValues() throws Exception {



        User oldUser = getRandomUser();
        String userId = oldUser.getId();

        // check if we can update just other values, not email, pass, username
        User updatingValueUser = new User();

        updatingValueUser.setFirstname("Jotie");
        updatingValueUser.setLastname("T'Hooft");
        updatingValueUser.setTeamspeak("jotie.teamspeak.com");
        updatingValueUser.setDiscord("https://discord.com/jotie");
        updatingValueUser.setDescription("I'm a poet, you don't rime");

        mockMvc.perform(post("/api/users/" + userId)
                .principal(new UserPrincipal(oldUser))
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstname", is("Jotie")))
                .andExpect(jsonPath("$.lastname", is("T'Hooft")))
                .andExpect(jsonPath("$.teamspeak", is("jotie.teamspeak.com")))
                .andExpect(jsonPath("$.discord", is("https://discord.com/jotie")))
                .andExpect(jsonPath("$.description", is("I'm a poet, you don't rime")));

        User updatedUser = userRepository.findOne(oldUser.getId());
        assertNotEquals(oldUser.getDescription(), updatedUser.getDescription());
        assertEquals(oldUser.getEmail(), userRepository.findByUsername(updatedUser.getUsername()).getEmail());
    }

    @Test
    public void addGamesToUser() throws Exception {
        User user = userRepository.findByUsername("wouter");

        Game game1 = this.gameList.get(2);
        Game game2 = this.gameList.get(4);

        List<String> gameIds = Arrays.asList(game1.getId(), game2.getId());

        mockMvc.perform(post("/api/users/" + user.getId() + "/games")
                .principal(new UserPrincipal(user))
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
                .contentType(contentType)
                .content(json(user)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.errors", hasKey("email")))
                .andExpect(jsonPath("$.errors.email", is("Email must be a valid email")));
    }

    @Test
    public void askForNonExistingUserTest() throws Exception {
        mockMvc.perform(get("/api/users?username=" + "asdfhjasdfhsjakldfhasjkdfhkjl")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }


    @Test
    public void addSkillLevelToUserGame() throws Exception {
        User user = userRepository.save(new User("gaming-user", "gaming@user.be", "Azerty123"));
        Game game = gameList.get(2);
        userGameRepository.save(new UserGame(user, game, 0));

        SkillLevelRequest skillLevel = new SkillLevelRequest();
        skillLevel.level = 15;

        mockMvc.perform(post("/api/users/" + user.getId() + "/games/" + game.getId())
                .principal(new UserPrincipal(user))
                .contentType(contentType)
                .content(json(skillLevel)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.id", is(game.getId())))
                .andExpect(jsonPath("$.skill_level", is(15)));

        UserGame ug = userGameRepository.findByUserAndGame(user, game);
        assertEquals(15, ug.getSkill_level());
    }





    @Test
    public void calculateGeneralScore() throws Exception {
        User user = userRepository.save(new User("skilledGamer", "skilledGamer@user.be", "Azerty123"));

        Game game1 = gameList.get(1);
        userGameRepository.save(new UserGame(user, game1, 0));

        Game game2 = gameList.get(2);
        userGameRepository.save(new UserGame(user, game2, 0));

        SkillLevelRequest skillLevel1 = new SkillLevelRequest();
        skillLevel1.level = 10;

        SkillLevelRequest skillLevel2 = new SkillLevelRequest();
        skillLevel2.level = 20;

        mockMvc.perform(post("/api/users/" + user.getId() + "/games/" + game1.getId())
                .principal(new UserPrincipal(user))
                .contentType(contentType)
                .content(json(skillLevel1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.id", is(game1.getId())))
                .andExpect(jsonPath("$.skill_level", is(10)));

        mockMvc.perform(post("/api/users/" + user.getId() + "/games/" + game2.getId())
                .principal(new UserPrincipal(user))
                .contentType(contentType)
                .content(json(skillLevel2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.game.id", is(game2.getId())))
                .andExpect(jsonPath("$.skill_level", is(20)));

        User u = userRepository.findByUsername(user.getUsername());
        assertEquals(15, u.getLevel());
    }

    @Test
    public void addUserWithUsernameThatAlreadyExists() throws Exception {
        User user = getRandomUser();
        User newUser = new User("my super cool username", user.getEmail(), "lalalala123");

//        mockMvc.perform(post("/api/users")
//                .contentType(contentType)
//                .content(json(newUser)))
//                .andExpect(status().isConflict());
    }


    @Test
    public void addUserWithEmailThatAlreadyExists() throws Exception {
        User user = getRandomUser();
        User newUser = new User(user.getUsername(), "copyc@t.com", "lalalala123");

//        mockMvc.perform(post("/api/users")
//                .contentType(contentType)
//                .content(json(newUser)))
//                .andExpect(status().isConflict());
    }

    @Test
    public void deleteGameFromUser() throws Exception {
        User user = userRepository.findByUsername("wouter");


        Game game1 = this.gameList.get(2);
        Game game2 = this.gameList.get(4);
        Game game3 = this.gameList.get(6);

        userGameRepository.save(new UserGame(user, game1, 0));
        userGameRepository.save(new UserGame(user, game2, 0));
        userGameRepository.save(new UserGame(user, game3, 0));

        int startLength = userGameRepository.findByUser(user).size();

        mockMvc.perform(delete("/api/users/" + user.getId() + "/games/" + game2.getId())
                .principal(new UserPrincipal(user)))
                .andExpect(status().isOk());

        assertEquals(startLength - 1, userGameRepository.findByUser(user).size());
        List<String> gameIdsByUser = userGameRepository
                .findByUser(user)
                .stream()
                .map(ug -> ug.getGame().getId())
                .collect(Collectors.toList());

        assertTrue(gameIdsByUser.contains(game1.getId()));
        assertTrue(gameIdsByUser.contains(game3.getId()));
        assertFalse(gameIdsByUser.contains(game2.getId()));
    }

    @Test
    public void addGameThatUserAlreadyHas() throws Exception {
        User user = userRepository.findByUsername("wouter");

        Game game1 = this.gameList.get(2);
        Game game2 = this.gameList.get(4);
        Game game3 = this.gameList.get(6);

        userGameRepository.save(new UserGame(user, game1, 0));
        userGameRepository.save(new UserGame(user, game2, 0));
        userGameRepository.save(new UserGame(user, game3, 0));

        List<String> gameIds = Arrays.asList(game2.getId());

        int startSize = userGameRepository.findByUser(user).size();


        mockMvc.perform(post("/api/users/" + user.getId() + "/games")
                .principal(new UserPrincipal(user))
                .contentType(contentType)
                .content(json(gameIds)))
                .andExpect(status().isConflict());

        assertEquals(userGameRepository.findByUser(user).size(), startSize);
    }

    @Test
    public void updateUserToExistingUsername() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // updated values
        User updatedUser = new User();
        updatedUser.setUsername(user1.getUsername());


        mockMvc.perform(post("/api/users/" + user2.getId())
                .principal(new UserPrincipal(user2))
                .contentType(contentType)
                .content(json(updatedUser)))
                .andExpect(status().isConflict());


        // expect user 1 to exist
        assertNotNull(this.userRepository.findByUsername(user1.getUsername()));

        // expect user 2 to exist
        assertNotNull(this.userRepository.findByUsername(user1.getUsername()));

        // expect user1 to still have its username
        assertEquals(user1.getId(), this.userRepository.findByUsername(user1.getUsername()).getId());

        // expect user2 to still have its old username
        assertEquals(user2.getUsername(), this.userRepository.findOne(user2.getId()).getUsername());

    }
    @Test
    public void updateUserWithSameUsername() throws Exception {
        User oldUser = getRandomUser();
        String userId = oldUser.getId();

        // check if we can update just other values, not email, pass, username
        User updatingValueUser = new User();

        updatingValueUser.setUsername(oldUser.getUsername());
        updatingValueUser.setLastname("T'Hooft");
        updatingValueUser.setTeamspeak("jotie.teamspeak.com");
        updatingValueUser.setDiscord("https://discord.com/jotie");
        updatingValueUser.setDescription("I'm a poet, you don't rime");

        mockMvc.perform(post("/api/users/" + userId)
                .principal(new UserPrincipal(oldUser))
                .contentType(contentType)
                .content(json(updatingValueUser)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(oldUser.getUsername())))
                .andExpect(jsonPath("$.lastname", is("T'Hooft")))
                .andExpect(jsonPath("$.teamspeak", is("jotie.teamspeak.com")))
                .andExpect(jsonPath("$.discord", is("https://discord.com/jotie")))
                .andExpect(jsonPath("$.description", is("I'm a poet, you don't rime")));

        User updatedUser = userRepository.findOne(oldUser.getId());
        assertNotEquals(oldUser.getDescription(), updatedUser.getDescription());
        assertEquals(oldUser.getEmail(), userRepository.findByUsername(updatedUser.getUsername()).getEmail());
    }


    @Test
    public void updateUserToExistingEmail() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // updated values
        User updatedUser = new User();
        updatedUser.setEmail(user1.getEmail());


        mockMvc.perform(post("/api/users/" + user2.getId())
                .principal(new UserPrincipal(user2))
                .contentType(contentType)
                .content(json(updatedUser)))
                .andExpect(status().isConflict());


        // expect user 1 to exist
        assertNotNull(this.userRepository.findByEmail(user1.getEmail()));

        // expect user 2 to exist
        assertNotNull(this.userRepository.findByEmail(user1.getEmail()));

        // expect user1 to still have its email
        assertEquals(user1.getId(), this.userRepository.findByEmail(user1.getEmail()).getId());

        // expect user2 to still have its old email
        assertEquals(user2.getEmail(), this.userRepository.findOne(user2.getId()).getEmail());

    }

    @Test
    public void TestPersonalMeRoute() throws Exception {
        User user = getRandomUser();

        mockMvc.perform(get("/api/users/me")
                .principal(new UserPrincipal(user)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username", is(user.getUsername())))
                .andExpect(jsonPath("$.email", is(user.getEmail())));
    }

    /* tests te schrijven:
        - user verwijderen die niet bestaat?
        - game toevoegen aan user die niet bestaat
        - game verwijderen van user die niet bestaat
        - game verwijderen die niet bestaat van user
        - ...
    */
}
