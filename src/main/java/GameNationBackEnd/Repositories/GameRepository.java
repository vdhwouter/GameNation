package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    Game findByName(String name);
    List<Game> findAll();
}
