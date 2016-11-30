package GameNationBackEnd.Repositories;

import java.util.List;
import GameNationBackEnd.Documents.Friend;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * Created by tijs on 30/11/2016.
 */
public interface FriendRepository extends MongoRepository<Friend, String> {
    List<Friend> findBySender(User sender);
    List<Friend> findByReceiver(User receiver);
    Friend findBySenderAndReceiver(User sender, User Receiver);
    List<Friend> findAll();
}
