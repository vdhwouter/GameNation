package GameNationBackEnd;

import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.*;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
    List<User> findAll();
}
