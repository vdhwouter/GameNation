package GameNationBackEnd.Repositories;

import GameNationBackEnd.Controllers.UsersController;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Repositories.UserRepository;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.List;
import java.util.Set;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserRepositoryTest extends TestCase {

    @Autowired
    UserRepository userRepo;

    private User user1 = new User("lucas", "lucas@gmail.com", "wat? ja inderdaad!");
    private User user2 = new User("matthias", "matthias@gmail.com", "mattie!");
    private User user3 = new User("wouter", "wouter@gmail.com", "superwow");
    private User user4 = new User("kjell", "kjell@gmail.com", "c <3");
    private User user5 = new User("tijs", "tijs@gmail.com", "tijsje123");

    private Validator validator;

    @Before
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();

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

//    @Test(expected = )
    @Test
    public void ValidateEmailTest() {
        User user = new User("lucaske", "lucas_at_gmail_com", "lalala");
        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertEquals( 1, violations.size() );
        assertEquals("Email must be a valid email", violations.iterator().next().getMessage());
    }
}