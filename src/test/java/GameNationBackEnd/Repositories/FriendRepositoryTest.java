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
    private Friend friend5 = new Friend(user1,user3);

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
        friendRepo.save(friend5);
    }

    @Test
    public void GetAllUsersDB() {
        List<Friend> all = friendRepo.findAll();
        assertEquals(5, all.size());
    }

    @Test
    public void GetReceiverBySender() {
        List<Friend> friends = friendRepo.findBySender(user1);
        assertEquals("matthias", friends.get(0).getReceiver().getUsername());
        assertEquals("wouter", friends.get(1).getReceiver().getUsername());
    }

    @Test
    public void GetSenderByReceiver() {
        List<Friend> friends = friendRepo.findByReceiver(user3);
        assertEquals("matthias", friends.get(0).getSender().getUsername());
        assertEquals("lucas", friends.get(1).getSender().getUsername());
    }

    @Test
    public void GetFriendBySenderAndReceiver() {
        Friend friend = friendRepo.findBySenderAndReceiver(user3,user4);
        assertEquals("wouter", friend.getSender().getUsername());
        assertEquals("kjell", friend.getReceiver().getUsername());
    }

    @Test
    public void GetAcceptedFriendsSender() {
        Friend friend6 = new Friend(user3,user1);
        Friend friend7 = new Friend(user3,user2);
        Friend friend8 = new Friend(user3,user3);
        Friend friend9 = new Friend(user3,user4);

        friend6.setAccepted(true);
        friend7.setAccepted(true);
        friend8.setAccepted(true);
        friend9.setAccepted(false);

        friendRepo.deleteAll();
        friendRepo.save(friend6);
        friendRepo.save(friend7);
        friendRepo.save(friend8);
        friendRepo.save(friend9);

        List<Friend> friends = friendRepo.findBySenderAndAccepted(user3,true);
        assertEquals("lucas", friends.get(0).getReceiver().getUsername());
        assertEquals("matthias", friends.get(1).getReceiver().getUsername());
        assertEquals("wouter", friends.get(2).getReceiver().getUsername());
    }

    @Test
    public void GetAcceptedFriendsReceiver() {

        Friend friend6 = new Friend(user1,user3);
        Friend friend7 = new Friend(user2,user3);
        Friend friend8 = new Friend(user3,user3);
        Friend friend9 = new Friend(user4,user3);

        friend6.setAccepted(true);
        friend7.setAccepted(true);
        friend8.setAccepted(true);
        friend9.setAccepted(false);

        friendRepo.deleteAll();
        friendRepo.save(friend6);
        friendRepo.save(friend7);
        friendRepo.save(friend8);
        friendRepo.save(friend9);

        List<Friend> friends = friendRepo.findByReceiverAndAccepted(user3,true);

        assertEquals("lucas", friends.get(0).getSender().getUsername());
        assertEquals("matthias", friends.get(1).getSender().getUsername());
        assertEquals("wouter", friends.get(2).getSender().getUsername());
    }

    @Test
    public void GetNotAcceptedFriendsSender() {
        Friend friend6 = new Friend(user2,user1);
        Friend friend7 = new Friend(user2,user2);
        Friend friend8 = new Friend(user2,user3);
        Friend friend9 = new Friend(user2,user4);

        friend6.setAccepted(false);
        friend7.setAccepted(false);
        friend8.setAccepted(false);
        friend9.setAccepted(true);

        friendRepo.deleteAll();
        friendRepo.save(friend6);
        friendRepo.save(friend7);
        friendRepo.save(friend8);
        friendRepo.save(friend9);

        List<Friend> friends = friendRepo.findBySenderAndAccepted(user2,false);
        assertEquals("lucas", friends.get(0).getReceiver().getUsername());
        assertEquals("matthias", friends.get(1).getReceiver().getUsername());
        assertEquals("wouter", friends.get(2).getReceiver().getUsername());
    }

    @Test
    public void GetNotAcceptedFriendsReceiver() {

        Friend friend6 = new Friend(user1,user2);
        Friend friend7 = new Friend(user2,user2);
        Friend friend8 = new Friend(user3,user2);
        Friend friend9 = new Friend(user4,user2);

        friend6.setAccepted(false);
        friend7.setAccepted(false);
        friend8.setAccepted(false);
        friend9.setAccepted(true);

        friendRepo.deleteAll();
        friendRepo.save(friend6);
        friendRepo.save(friend7);
        friendRepo.save(friend8);
        friendRepo.save(friend9);

        List<Friend> friends = friendRepo.findByReceiverAndAccepted(user2,false);

        assertEquals("lucas", friends.get(0).getSender().getUsername());
        assertEquals("matthias", friends.get(1).getSender().getUsername());
        assertEquals("wouter", friends.get(2).getSender().getUsername());
    }
}
