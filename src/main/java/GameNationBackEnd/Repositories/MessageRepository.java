package GameNationBackEnd.Repositories;

import java.util.List;
import GameNationBackEnd.Documents.Message;
import GameNationBackEnd.Documents.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findBySender(User sender);
    List<Message> findByReceiver(User receiver);
    List<Message> findBySenderAndReceiver(User sender, User receiver);
}
