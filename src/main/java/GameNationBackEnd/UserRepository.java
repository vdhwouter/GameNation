package GameNationBackEnd;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    //User addGameToUser(User user,Game game);
    List<User> findAll();
}
