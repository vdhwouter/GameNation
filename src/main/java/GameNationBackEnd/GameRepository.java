package GameNationBackEnd;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GameRepository extends MongoRepository<Game, String> {
    Game findByName(String name);
    List<Game> findAll();
}
