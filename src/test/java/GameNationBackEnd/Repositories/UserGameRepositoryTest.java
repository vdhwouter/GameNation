package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserGameRepositoryTest extends TestCase {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserGameRepository userGameRepository;

    @Autowired
    GameRepository gameRepository;

    List<User> userList;
    List<Game> gameList;

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

        userGameRepository.deleteAll();
        userRepository.deleteAll();
        gameRepository.deleteAll();

        userList = new ArrayList<>();
        gameList = new ArrayList<>();

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
    public void GetAllUserGamesByUser() {
        User user = this.userList.get(0);

        userGameRepository.save(new UserGame(user, gameRepository.findAll().get(0), 0));
        userGameRepository.save(new UserGame(user, gameRepository.findAll().get(1), 0));
        userGameRepository.save(new UserGame(user, gameRepository.findAll().get(2), 0));
        userGameRepository.save(new UserGame(user, gameRepository.findAll().get(3), 0));

        List<UserGame> allGamesByUser = userGameRepository.findByUser(user);
        assertEquals(4, allGamesByUser.size());
    }
}