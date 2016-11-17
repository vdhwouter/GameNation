package GameNationBackEnd;

import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    User findByUsername(String username);
    //User addGameToUser(User user,Game game);
    List<User> findAll();
}
