package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tijs on 30/11/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GameRepositoryTest extends TestCase {
    @Autowired
    private GameRepository gameRepo;

    @Before
    public void setUp() {
        gameRepo.deleteAll();

        // 7 games
        gameRepo.save(new Game("super mario", "race your friends", "mario.jpg"));
        gameRepo.save(new Game("counter-strike", "kill them instead", "cs.jpg"));
        gameRepo.save(new Game("payday", "shooting i guess", "payday.jpg"));
        gameRepo.save(new Game("windows movie maker", "not a game", "wmm.jpg"));
        gameRepo.save(new Game("civilization 6", "conquer", "civ.jpg"));
        gameRepo.save(new Game("dota 2", "free they say", "dota.jpg"));
        gameRepo.save(new Game("team fortress 2", "still popular", "wmm.jpg"));
    }

    @Test
    public void GetAllGamesDB() {
        List<Game> all = gameRepo.findAll();
        assertEquals(7, all.size());
    }

    @Test
    public void GetGameByNameTest(){
        assertEquals("payday", gameRepo.findByName("payday").getName());
    }
}