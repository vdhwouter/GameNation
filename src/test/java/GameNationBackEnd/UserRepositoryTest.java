package GameNationBackEnd;

import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest extends TestCase {

    @Autowired
    UserRepository userRepo;

    @Before
    public void setUp() {
        User user1 = new User("lucas", "lucas@gmail.com", "wat? ja inderdaad!");
        User user2 = new User("matthias", "matthias@gmail.com", "mattie!");
        User user3 = new User("wouter", "wouter1234@gmail.com", "superwow");
        User user4 = new User("kjell", "tijs@gmail.com", "c <3");
        User user5 = new User("tijs", "tijs@gmail.com", "tijsje123");

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);
        userRepo.save(user5);
    }

    @Test
    public void happyTest() {
        List<User> all = userRepo.findAll();
        assertEquals(5, all.size());

        User user = userRepo.findByUsername("lucas");
        assertEquals("lucas@gmail.com", user.getEmail());
    }
}