package GameNationBackEnd.Security;

import GameNationBackEnd.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

/**
 * Created by lucas on 19/11/2016.
 */
@Configuration
public class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {

    @Autowired
    UserRepository userRepository;

    @Override
    public void init(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService());
    }

    @Bean
    // finds user by username and gives the user its role
    UserDetailsService userDetailsService() {
        // todo: now everyone is just user. We want users AND admins. here is the place to assign correct role.
        return (username) -> {
            GameNationBackEnd.Documents.User user = userRepository.findByUsername(username);

            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

            return new User(user.getUsername(), user.getPassword(), true, true, true, true,
                    AuthorityUtils.createAuthorityList("USER", "write"));
        };
    }

}
