package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.Game;
import GameNationBackEnd.Documents.UserGame;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGameRepository extends MongoRepository<UserGame, String> {
    List<UserGame> findByUser(User user);
    List<UserGame> findAll();

    // da dingske werkt hier precies nie echt kweenie, steeds NullPointerExceptions
    // misschiend door die dbrefs? kweenie
    List<Game> findGameByUser(User user);


    UserGame findByUserAndGame(User user, Game game);
}
