package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.User;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * Created by tijs on 30/11/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class FriendRepositoryTest extends TestCase {

    @Autowired
    private FriendRepository friendRepo;

    @Autowired
    private UserRepository userRepo;

    private User user1 = new User("lucas", "lucas@gmail.com", "wat? ja inderdaad!");
    private User user2 = new User("matthias", "matthias@gmail.com", "mattie!");
    private User user3 = new User("wouter", "wouter@gmail.com", "superwow");
    private User user4 = new User("kjell", "kjell@gmail.com", "c <3");
    private User user5 = new User("tijs", "tijs@gmail.com", "tijsje123");

    private Friend friend1 = new Friend(user1,user2);
    private Friend friend2 = new Friend(user2,user3);
    private Friend friend3 = new Friend(user3,user4);
    private Friend friend4 = new Friend(user4,user5);

    @Before
    public void setUp() {
        friendRepo.deleteAll();
        userRepo.deleteAll();

        userRepo.save(user1);
        userRepo.save(user2);
        userRepo.save(user3);
        userRepo.save(user4);
        userRepo.save(user5);

        friendRepo.save(friend1);
        friendRepo.save(friend2);
        friendRepo.save(friend3);
        friendRepo.save(friend4);
    }

    @Test
    public void GetAllUsersDB() {
        List<Friend> all = friendRepo.findAll();
        assertEquals(4, all.size());
    }

    @Test
    public void GetReceiverBySender() {
        Friend friend = friendRepo.findBySender(user1);
        assertEquals("matthias", friend.getReceiver().getUsername());
    }

    @Test
    public void GetSenderByReceiver() {
        Friend friend = friendRepo.findByReceiver(user3);
        assertEquals("matthias", friend.getSender().getUsername());
    }

    @Test
    public void GetFriendBySenderAndReceiver() {
        Friend friend = friendRepo.findBySenderAndReceiver(user3,user4);
        assertEquals("wouter", friend.getSender().getUsername());
        assertEquals("kjell", friend.getReceiver().getUsername());
    }
}
