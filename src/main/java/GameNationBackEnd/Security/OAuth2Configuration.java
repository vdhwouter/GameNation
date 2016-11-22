package GameNationBackEnd.Security;

import GameNationBackEnd.Security.DevToken.UnlimitedAuthentication;
import GameNationBackEnd.Security.DevToken.UnlimitedToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;

import java.io.Serializable;
import java.util.*;

@Configuration
@EnableAuthorizationServer
public class OAuth2Configuration extends AuthorizationServerConfigurerAdapter {

    public static String ApplicationName = "gamenation";

    // This is required for password grants, which we specify below as one of the options
    @Autowired
    AuthenticationManagerBuilder authenticationManager;

    @Autowired
    TokenStore tokenStore;

    @Bean
    public TokenStore tokenStore() {
        TokenStore store = new InMemoryTokenStore();

        // add new token to store, this is an unlimited token to use during testing
        UnlimitedToken token = new UnlimitedToken();
        UnlimitedAuthentication unlimitedAuthentication = new UnlimitedAuthentication();
        OAuth2Request request = new OAuth2Request(new HashMap<String, String>(), "web-gamenation", unlimitedAuthentication.getAuthorities(), true, token.getScope(), null, "", new HashSet<String>(), new HashMap<String, Serializable>());
        OAuth2Authentication oAuth2Authentication = new OAuth2Authentication(request, unlimitedAuthentication);
        store.storeAccessToken(token, oAuth2Authentication);

        return store;
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.tokenStore(tokenStore);
        endpoints.authenticationManager(new AuthenticationManager() {
            @Override
            public Authentication authenticate(Authentication authentication)
                    throws AuthenticationException {
                return authenticationManager.getOrBuild().authenticate(authentication);
            }
        });
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("web-" + ApplicationName)
                .authorizedGrantTypes("password", "authorization_code", "refresh_token")
                .authorities("ROLE_USER")
                .scopes("write")
                .resourceIds(ApplicationName)
                .secret("123456");
    }

}