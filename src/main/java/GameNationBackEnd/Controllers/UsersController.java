package GameNationBackEnd.Controllers;

import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Repositories.FriendRepository;
import GameNationBackEnd.RequestDocuments.FriendRequest;
import GameNationBackEnd.RequestDocuments.SkillLevelRequest;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import GameNationBackEnd.Repositories.GameRepository;
import GameNationBackEnd.Repositories.UserGameRepository;
import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import GameNationBackEnd.Exceptions.*;

/**
 * Created by lucas on 17/11/2016.
 */
@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UsersController {
    @Autowired
    private UserRepository userDB;

    @Autowired
    private UserGameRepository userGameDB;

    @Autowired
    private GameRepository gameRepository;

    @Autowired
    private FriendRepository friendRepository;

    private BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    // get all users
    @RequestMapping(method = RequestMethod.GET)
    public List<User> GetUsers() {
        return userDB.findAll().stream().map(user -> {
            user.setPassword(null);
            return user;
        }).collect(Collectors.toList());
    }


    @RequestMapping(value = "/testcase", method = RequestMethod.GET, params = {"username"})
    public User TestCase(@RequestParam String username) {
        User user = userDB.findByUsernameIgnoreCase(username);
        user.setPassword(null);
        return user;
    }


    // get user by username
    @RequestMapping(method = RequestMethod.GET, params = {"username"})
    public List<User> GetUserByUsername(@RequestParam String username, Principal principal) {
        User user = userDB.findByUsernameIgnoreCase(username);
        if (user != null) {
            // if authenticated, add relation between users.
            if (principal != null) {
                User principalUser = userDB.findByUsernameIgnoreCase(principal.getName());
                user.setRelation(getRelation(user, principalUser));
                user.setPassword(null);
            }

            return Arrays.asList(user);
        } else {
            return new ArrayList<>();
        }
    }

    // get user by id
    @RequestMapping(value = "/{user}", method = RequestMethod.GET)
    public User GetUser(@PathVariable User user, Principal principal) {
        // if authenticated, add relation between users.
        if (principal != null) {
            User principalUser = userDB.findByUsernameIgnoreCase(principal.getName());
            user.setRelation(getRelation(user, principalUser));
            user.setPassword(null);
        }
        return user;
    }

    // update user
    @RequestMapping(value = "/{user}", method = RequestMethod.POST)
    public User UpdateUser(@PathVariable User user, @RequestBody User updatedUser, Principal principal) throws Exception {
        if (principal == null || !user.getUsername().equalsIgnoreCase(principal.getName())) {
            throw new NotAuthorizedException();
        }

        if(!(userDB.findByEmail(updatedUser.getEmail()) == null) && !(userDB.findByEmail(updatedUser.getEmail()).getId().equals(userDB.findByEmail(user.getEmail()).getId()))) {
            throw new UserEmailAlreadyExistsException(updatedUser.getEmail());
        }

        if(userDB.findByUsername(updatedUser.getUsername()) == null || (userDB.findByUsername(updatedUser.getUsername()).getId().equals(userDB.findByUsername(user.getUsername()).getId()))){
            if (updatedUser.getUsername() != null) user.setUsername(updatedUser.getUsername());
            if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
            if (updatedUser.getPassword() != null) user.setPassword(bCryptPasswordEncoder.encode(updatedUser.getPassword()));
            if (updatedUser.getFirstname() != null) user.setFirstname(updatedUser.getFirstname());
            if (updatedUser.getLastname() != null) user.setLastname(updatedUser.getLastname());
            if (updatedUser.getTeamspeak() != null) user.setTeamspeak(updatedUser.getTeamspeak());
            if (updatedUser.getDiscord() != null) user.setDiscord(updatedUser.getDiscord());
            if (updatedUser.getDescription() != null) user.setDescription(updatedUser.getDescription());

            if (updatedUser.getAvatar() != null) user.setAvatar(updatedUser.getAvatar());

            userDB.delete(user);
            userDB.save(user);
        } else {
            throw new UserAlreadyExistsException(updatedUser.getUsername());
        }

        user.setPassword(null);
        return user;
    }

    // create a user
    @RequestMapping(method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.CREATED)
    public User InsertUser(@Valid @RequestBody User user) throws UserAlreadyExistsException, UserEmailAlreadyExistsException{
        if (userDB.findByUsernameIgnoreCase(user.getUsername()) != null) {
            throw new UserAlreadyExistsException(user.getUsername());
        } else if(userDB.findByEmail(user.getEmail()) != null){
            throw new UserEmailAlreadyExistsException(user.getEmail());
        }else {
            user.setAvatar("img/avatar-member.jpg");
            user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
            User newUser = userDB.save(user);
            newUser.setPassword(null);
            return newUser;
        }
    }

    // delete user
    @RequestMapping(value = "/{user}", method = RequestMethod.DELETE)
    public void DeleteUser(@PathVariable User user) {
        userDB.delete(user);

        userGameDB.findByUser(user).stream().forEach(ug -> userGameDB.delete(ug));
        friendRepository.findByReceiver(user).stream().forEach(fr -> friendRepository.delete(fr));
        friendRepository.findBySender(user).stream().forEach(fr -> friendRepository.delete(fr));
    }

    // get games for user
    @RequestMapping(value = "/{user}/games", method = RequestMethod.GET)
    public List<UserGame> GetGamesFromUser(@PathVariable User user) {
        return userGameDB.findByUser(user);
    }

    // add games to user
    @RequestMapping(value = "/{user}/games", method = RequestMethod.POST)
    public List<UserGame> AddGamesToUser(@PathVariable User user, @RequestBody List<String> games, Principal principal) {
        if (principal == null || !user.getUsername().equalsIgnoreCase(principal.getName())) {
            throw new NotAuthorizedException();
        }
        
        List<Game> gameList = gameRepository.findByIdIn(games);

        // check if any of the games already exist in the user's collection
        userGameDB.findByUser(user).stream().map(ug -> ug.getGame()).forEach(game -> {
            gameList.forEach(g -> {
                if (g.equals(game)) throw new GameAlreadyExistsException(game.getName());
            });
        });

        for (Game game : gameList) userGameDB.save(new UserGame(user,game, 0));

        return userGameDB.findByUser(user);
    }

    // set skill on game for user
    @RequestMapping(value = "/{user}/games/{game}", method = RequestMethod.POST)
    public UserGame setSkillLevelForUserGame(@PathVariable User user, @PathVariable Game game, @RequestBody SkillLevelRequest skillLevel, Principal principal) {
        if (principal == null || !user.getUsername().equalsIgnoreCase(principal.getName())) throw new NotAuthorizedException();

        UserGame ug = userGameDB.findByUserAndGame(user, game);
        ug.setSkill_level(skillLevel.level);
        userGameDB.save(ug);

        setGeneralSkill(user);

        return userGameDB.findByUserAndGame(user, game);
    }

    private void setGeneralSkill(User user){

        int totalLevel = 0, countLevel = 0;

        for (UserGame singleGame : userGameDB.findByUser(user)) {
            totalLevel += singleGame.getSkill_level();
            countLevel++;
        }

        user.setLevel(totalLevel / countLevel);

        userDB.save(user);
    }

    // delete game from user
    @RequestMapping(value = "/{user}/games/{game}", method = RequestMethod.DELETE)
    public void deleteGameFromUser(@PathVariable User user, @PathVariable Game game, Principal principal) {
        if (principal == null || !user.getUsername().equalsIgnoreCase(principal.getName())) {
            throw new NotAuthorizedException();
        }
        
        UserGame ug = userGameDB.findByUserAndGame(user, game);
        userGameDB.delete(ug);
    }

    // get logged in user
    @RequestMapping(value = "/me", method = RequestMethod.GET)
    public User GetAuthenticatedUser(Principal principal) {
        if (principal == null) {
            throw new NotAuthorizedException();
        }
        // principal contains authenticated user name
        User user = userDB.findByUsernameIgnoreCase(principal.getName());
        user.setPassword(null);
        return user;
    }

    /* FRIENDS AREA */

    @RequestMapping(value = "/{user}/friends", method = RequestMethod.GET)
    public List<User> GetFriendsForUser(@PathVariable User user) {
        Stream<User> receivedStream = friendRepository.findByReceiverAndAccepted(user, true)
                .stream()
                .map(f -> f.getSender());

        Stream<User> sentStream = friendRepository.findBySenderAndAccepted(user, true)
                .stream()
                .map(f -> f.getReceiver());

        return Stream.concat(receivedStream, sentStream).collect(Collectors.toList());
    }

    @RequestMapping(value = "/{user}/friendrequests", method = RequestMethod.GET)
    public List<User> GetFriendrequestsForUser(@PathVariable User user, @RequestParam(value="direction", required = false, defaultValue = "both") String direction) {
        switch (direction) {
            case "from":
                return friendRepository
                        .findBySenderAndAccepted(user, false)
                        .stream()
                        .map(friend -> friend.getReceiver())
                        .collect(Collectors.toList());
            case "to":
                return friendRepository
                        .findByReceiverAndAccepted(user, false)
                        .stream()
                        .map(friend -> friend.getSender())
                        .collect(Collectors.toList());

            case "both":
                Stream<User> receivedStream = friendRepository.findByReceiverAndAccepted(user, false)
                        .stream()
                        .map(f -> f.getSender());

                Stream<User> sentStream = friendRepository.findBySenderAndAccepted(user, false)
                        .stream()
                        .map(f -> f.getReceiver());

                return Stream.concat(receivedStream, sentStream).collect(Collectors.toList());

            default:
                throw new IllegalArgumentException("direction can only have the following values: [from, to, both]");
        }
    }


    @RequestMapping(value = "/{sender}/friends", method = RequestMethod.POST)
    public Friend CreateOrAcceptFriendRequest(@PathVariable User sender, @RequestBody FriendRequest friendRequest, Principal principal) {
        if (principal == null || !sender.getUsername().equalsIgnoreCase(principal.getName())) {
            throw new NotAuthorizedException();
        }
        
        User receiver = userDB.findOne(friendRequest.user);

        // is there a relation in place already?
        Friend relation = getRelation(sender, receiver);

        // no relation yet? create one from the sender to the receiver
        if (relation == null) {
            return friendRepository.save(new Friend(sender, receiver));
        }

        // is there a relation?
        // check if it was started by the receiver
        if (relation.getSender().getId().equals(receiver.getId())) {
            // if so, accept and return
            relation.setAccepted(true);
            return friendRepository.save(relation);
        }

        // final option:
        // if sender has resent the request, just return again.
        return relation;
    }

    @RequestMapping(value = "/{user}/friends/{friend}", method = RequestMethod.DELETE)
    public void DeclineOrRemoveFriendship(@PathVariable User user, @PathVariable User friend, Principal principal) {
        if (principal == null || !user.getUsername().equalsIgnoreCase(principal.getName())) {
            throw new NotAuthorizedException();
        }
        
        // is there a relation?
        Friend relation = getRelation(user, friend);

        if (relation == null) {
//            throw error?
        }

        friendRepository.delete(relation);
    }

    private Friend getRelation(User user1, User user2) {
        Friend relation = friendRepository.findBySenderAndReceiver(user1, user2);
        if (relation == null) {
            return friendRepository.findBySenderAndReceiver(user2, user1);
        }

        return relation;
    }
}