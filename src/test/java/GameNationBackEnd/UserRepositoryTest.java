package GameNationBackEnd;

import GameNationBackEnd.Controllers.UsersController;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Repositories.UserRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class UserRepositoryTest extends TestCase {

    @Autowired
    UserRepository userRepo;

    UsersController usersControllerObj;

    private User user1 = new User("lucas", "lucas@gmail.com", "wat? ja inderdaad!");
    private User user2 = new User("matthias", "matthias@gmail.com", "mattie!");
    private User user3 = new User("wouter", "wouter@gmail.com", "superwow");
    private User user4 = new User("kjell", "kjell@gmail.com", "c <3");
    private User user5 = new User("tijs", "tijs@gmail.com", "tijsje123");

    @Before
    public void setUp() {
        userRepo.deleteAll();

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);
        userRepo.save(user5);
    }


    @Test
    public void GetAllUsersDB() {
        List<User> all = userRepo.findAll();
        assertEquals(5, all.size());
    }

    @Test
    public void GetUserByUsername() {
        User user = userRepo.findByUsername("lucas");
        assertEquals("lucas@gmail.com", user.getEmail());
    }

//    @Test
//    public void UpdateUser(){
//        usersControllerObj = new UsersController();
//        usersControllerObj.UpdateUser(user2, "", "", "", "", "", "", "", "This is a test");
//        User dbUser = userRepo.findByUsername("lucas");
//        assertEquals(dbUser.getDescription(), "This is a test");
//    }
}