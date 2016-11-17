package GameNationBackEnd;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface UserGameRepository extends MongoRepository<UserGame, String> {
    User findByUser(User user);
    List<UserGame> findAll();
}
