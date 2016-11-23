package GameNationBackEnd;

import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Filters.RoutingFilter;
import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.Arrays;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }


    // during dev only!
    // add super user for super token
    @Bean
    CommandLineRunner init(UserRepository userRepository) {
        return (evt) -> Arrays.asList(
                "test".split(","))
                .forEach(
                        name -> {
                            if (userRepository.findByUsername(name) == null) {
                                userRepository.save(new User(name, name + "@test.com", "test"));
                            }
                        });
    }

    @Bean
    RoutingFilter routingFilter() {
        return new RoutingFilter();
    }
}
