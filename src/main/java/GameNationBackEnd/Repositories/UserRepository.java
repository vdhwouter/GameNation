package GameNationBackEnd.Repositories;

import GameNationBackEnd.Documents.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface UserRepository extends CrudRepository<User, String> {
    // Deprecated, ingore case can be used everywhere (still used in tests)
    User findByUsername(String username);
    User findByUsernameIgnoreCase(String username);
    User findByEmail(String email);
    List<User> findAll();
}
