package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Repositories.FriendRepository;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import GameNationBackEnd.RequestDocuments.FriendRequest;
import GameNationBackEnd.Setup.BaseControllerTest;
import GameNationBackEnd.Setup.UserPrincipal;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by lucas on 30/11/2016.
 */
public class UserControllerFriendTest extends BaseControllerTest {

    private List<Game> gameList = new ArrayList<>();
    private List<User> userList = new ArrayList<>();

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private UserGameRepository userGameRepository;

    @Autowired
    private FriendRepository friendRepository;

    @Before
    public void setup() throws Exception {
        this.userGameRepository.deleteAll();
        this.userRepository.deleteAll();
        this.gameRepository.deleteAll();

        // 7 users
        this.userList.add(userRepository.save(new User("lucas", "lucas@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("kjell", "kjell@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("tijs", "tijs@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("matthias", "matthias@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("wouter", "wouter@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("poetie", "poetie@gmail.com", "Azerty123")));
        this.userList.add(userRepository.save(new User("oberon", "oberon@gmail.com", "Azerty123")));

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
    public void GetFriendsForUser() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        User user5 = userList.get(4);
        User user6 = userList.get(5);

        // receive friend request from user 2 and 4 and accept
        Friend friend1 = friendRepository.save(new Friend(user2, user1, true));
        Friend friend3 = friendRepository.save(new Friend(user4, user1, true));

        // send friend request to user 3 and accept
        Friend friend2 = friendRepository.save(new Friend(user1, user3, true));

        // add unaccepted requests to test if only friends are returned
        friendRepository.save(new Friend(user1, user5));
        friendRepository.save(new Friend(user6, user1));

        mockMvc.perform(get("/api/users/" + user1.getId() + "/friends")
                .contentType(contentType))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void GetFriendRequestForUser() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        User user5 = userList.get(4);
        User user6 = userList.get(5);

        // receive friend request from user 2 and 4
        Friend friend1 = friendRepository.save(new Friend(user2, user1));
        Friend friend3 = friendRepository.save(new Friend(user4, user1));

        // send friend request to user 3
        Friend friend2 = friendRepository.save(new Friend(user1, user3));

        // add 2 users that are already user1's friend
        Friend friend4 = friendRepository.save(new Friend(user5, user1, true));
        Friend friend5 = friendRepository.save(new Friend(user1, user6, true));

        mockMvc.perform(get("/api/users/" + user1.getId() + "/friendrequests?direction=to")
                .contentType(contentType)
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // TODO: extend test expectations when structure is known
    }

    @Test
    public void GetSentFriendrequestsForUser() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        User user5 = userList.get(4);
        User user6 = userList.get(5);

        // send friend request to user 2 and 4
        Friend friend1 = friendRepository.save(new Friend(user1, user2));
        Friend friend2 = friendRepository.save(new Friend(user1, user4));

        // receive friend request from user 3
        Friend friend3 = friendRepository.save(new Friend(user3, user1));

        // add 2 users that are already user1's friend
        Friend friend4 = friendRepository.save(new Friend(user5, user1, true));
        Friend friend5 = friendRepository.save(new Friend(user1, user6, true));

        // so user should have sent 2 friendrequests
        mockMvc.perform(get("/api/users/" + user1.getId() + "/friendrequests?direction=from")
                .contentType(contentType)
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));

        // TODO: extend test expectations when structure is known
    }

    @Test
    public void GetSentAndReceivedFriendrequestsForUser() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        User user5 = userList.get(4);
        User user6 = userList.get(5);
        User user7 = userList.get(6);

        // send friend request to user 2 and 4
        Friend friend1 = friendRepository.save(new Friend(user1, user2));
        Friend friend2 = friendRepository.save(new Friend(user1, user4));

        // receive friend request from user 3 and 5
        Friend friend3 = friendRepository.save(new Friend(user3, user1));
        Friend friend4 = friendRepository.save(new Friend(user5, user1));

        // add users that the user is already friends with
        Friend friend5 = friendRepository.save(new Friend(user6, user1, true));
        Friend friend6 = friendRepository.save(new Friend(user1, user7, true));

        // so total should be 4
        mockMvc.perform(get("/api/users/" + user1.getId() + "/friendrequests")
                .contentType(contentType)
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        // TODO: extend test expectations when structure is known
    }

    @Test
    public void GetFriendRequestsWithDirectionBoth() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);
        User user3 = userList.get(2);
        User user4 = userList.get(3);
        User user5 = userList.get(4);
        User user6 = userList.get(5);
        User user7 = userList.get(6);

        // send friend request to user 2 and 4
        Friend friend1 = friendRepository.save(new Friend(user1, user2));
        Friend friend2 = friendRepository.save(new Friend(user1, user4));

        // receive friend request from user 3 and 5
        Friend friend3 = friendRepository.save(new Friend(user3, user1));
        Friend friend4 = friendRepository.save(new Friend(user5, user1));

        // add users that the user is already friends with
        Friend friend5 = friendRepository.save(new Friend(user6, user1, true));
        Friend friend6 = friendRepository.save(new Friend(user1, user7, true));

        // so total should be 4
        mockMvc.perform(get("/api/users/" + user1.getId() + "/friendrequests?direction=both")
                .contentType(contentType)
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));

        // TODO: extend test expectations when structure is known
    }

    @Test
    public void AddFriend() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // friend request from user 1 to user 2
        FriendRequest request = new FriendRequest(user2);
        mockMvc.perform(post("/api/users/" + user1.getId() + "/friends")
                .principal(new UserPrincipal(user1))
                .contentType(contentType)
                .content(json(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sender.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.receiver.username", is(user2.getUsername())))
                .andExpect(jsonPath("$.accepted", is(false)));


        assertNotNull(friendRepository.findBySenderAndReceiver(user1, user2));
        assertFalse(friendRepository.findBySenderAndReceiver(user1,user2).isAccepted());
    }

    @Test
    public void AcceptFriend() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 2 requests to be friends
        friendRepository.save(new Friend(user2, user1));

        // user 1 accepts friend request
        mockMvc.perform(post("/api/users/" + user1.getId() + "/friends")
                .principal(new UserPrincipal(user1))
                .contentType(contentType)
                .content(json(new FriendRequest(user2))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.sender.username", is(user2.getUsername())))
                .andExpect(jsonPath("$.receiver.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.accepted", is(true)));

        assertNotNull(friendRepository.findBySenderAndReceiver(user2, user1));
        assertTrue(friendRepository.findBySenderAndReceiver(user2, user1).isAccepted());
    }

    @Test
    public void DeclineFriendRequest() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 2 requests to be friends
        friendRepository.save(new Friend(user2, user1));

        // user 1 declines friend request
        mockMvc.perform(delete("/api/users/" + user1.getId() + "/friends/" + user2.getId())
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk());

        assertNull(friendRepository.findBySenderAndReceiver(user2, user1));
        assertNull(friendRepository.findBySenderAndReceiver(user1, user2));
    }


    @Test
    public void EndFriendship() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 1 and 2 are friends
        friendRepository.save(new Friend(user1, user2, true));

        // user 1 ends the friendship
        mockMvc.perform(delete("/api/users/" + user1.getId() + "/friends/" + user2.getId())
                .principal(new UserPrincipal(user1)))
                .andExpect(status().isOk());

        assertNull(friendRepository.findBySenderAndReceiver(user2, user1));
        assertNull(friendRepository.findBySenderAndReceiver(user1, user2));
    }


    @Test
    public void GetUserThatIsFriend() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 1 and 2 are friends
        friendRepository.save(new Friend(user1, user2, true));

        mockMvc.perform(get("/api/users/" + user1.getId())
                .principal(new UserPrincipal(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation", notNullValue()))
                .andExpect(jsonPath("$.relation.accepted", is(true)))
                .andExpect(jsonPath("$.relation.sender.username", is(user1.getUsername())))
                .andExpect(jsonPath("$.relation.receiver.username", is(user2.getUsername())));
    }

    @Test
    public void GetUserThatIsNotFriend() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 1 and 2 are not friends.

        mockMvc.perform(get("/api/users/" + user1.getId())
                .principal(new UserPrincipal(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.relation").doesNotExist());
    }

    @Test
    public void GetUserByUsernameThatIsFriend() throws Exception {
        User user1 = userList.get(0);
        User user2 = userList.get(1);

        // user 1 and 2 are friends
        friendRepository.save(new Friend(user1, user2, true));

        mockMvc.perform(get("/api/users?username=" + user1.getUsername())
                .principal(new UserPrincipal(user2)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].relation", notNullValue()))
                .andExpect(jsonPath("$[0].relation.accepted", is(true)))
                .andExpect(jsonPath("$[0].relation.sender.username", is(user1.getUsername())))
                .andExpect(jsonPath("$[0].relation.receiver.username", is(user2.getUsername())));
    }
}
