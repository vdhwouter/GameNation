package GameNationBackEnd.Mongo;

import com.mongodb.MongoClient;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

/**
 * Created by lucas on 17/11/2016.
 */

@EnableMongoRepositories
public class SpringMongoConfiguration {

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        // return embedded mongo client address
        return new MongoTemplate(new MongoClient("localhost", 12345), "test-database-do-not-use");
    }

}