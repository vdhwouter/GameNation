package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Repositories.FriendRepository;
import GameNationBackEnd.Repositories.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by tijs on 30/11/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/friends")
public class FriendController{

        @Autowired
        private FriendRepository friendDB;

        // get all games
        @RequestMapping(method = RequestMethod.GET)
        public List<Friend> GetAllFriends() {
            return friendDB.findAll();
        }


        @RequestMapping(method = RequestMethod.POST)
        @ResponseStatus(value = HttpStatus.CREATED)
        public Friend InsertFriend(@RequestBody User sender, @RequestBody User receiver) {
            Friend friends = new Friend(sender,receiver);
            return friendDB.save(friends);
        }

        // get single game by id
        @RequestMapping(value = "/{friend}", method = RequestMethod.GET)
        public Friend GetFriend(@PathVariable Friend friend) {
            return friend;
        }

        @RequestMapping(value = "/{friend}", method = RequestMethod.DELETE)
        public void DeleteFriends(@PathVariable Friend friend) {
            friendDB.delete(friend);
        }
}
