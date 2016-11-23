package GameNationBackEnd.Security;

import GameNationBackEnd.Repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;

/**
 * Created by lucas on 23/11/2016.
 */

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuhenticationTest {

    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    HttpHeaders headers = new HttpHeaders();

    @Before
    public void setup() {
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.getRestTemplate().getMessageConverters().add(new MappingJackson2HttpMessageConverter());
        restTemplate.getRestTemplate().getMessageConverters().add(new StringHttpMessageConverter());
    }

    @Test
    public void getAuthenticationToken() throws IOException {
        MultiValueMap<String, String> postParams = new LinkedMultiValueMap<String, String>();
        postParams.add("username", "test");
        postParams.add("password", "test");
        postParams.add("grant_type", "password");

//        restTemplate.postForEntity()

        String res = restTemplate
                .withBasicAuth("web-gamenation", "123456")
                .postForObject("/oauth/token", postParams, String.class);
    }

    class OAuthRequest {
        public String username;
        public String password;
        public String grant_type;

        public String GetUsername() {return username;}
        public String GetPassword() {return password;}
        public String GetGrant_type() {return grant_type;}
    }

    class OAuthResponse {
        String access_token;
        String token_type;
        String refresh_token;
        String expires_in;
        String scope;
    }
}
