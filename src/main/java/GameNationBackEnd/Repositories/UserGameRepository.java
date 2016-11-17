package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.UserGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGameRepository extends MongoRepository<UserGame, String> {
    UserGame findByUser(User user);
    List<UserGame> findAll();
}
