package hello;

import org.springframework.data.mongodb.repostitory.MongoRepository;

public interface UserRepostitory extends MongoRepository<User, String> {
    User findByName(String name);
}
