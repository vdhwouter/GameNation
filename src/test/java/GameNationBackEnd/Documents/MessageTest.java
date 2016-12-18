package GameNationBackEnd.Documents;



import GameNationBackEnd.Documents.*;
import GameNationBackEnd.Filters.RoutingFilter;
import GameNationBackEnd.Repositories.*;
import GameNationBackEnd.Setup.BaseControllerTest;
import GameNationBackEnd.RequestDocuments.SkillLevelRequest;
import GameNationBackEnd.Setup.UserPrincipal;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Created by MatthiasMaes on 18/12/2016.
 */
public class MessageTest {

    private User sender = new User("matthias", "matthias@gmail.com", "Azerty123");
    private User receiver = new User("tijs", "tijs@gmail.com", "Azerty123");
    private String message = "Test bericht";
    Message message1;

    @Before
    public void CreateMessageObject(){
        message1 = new Message(sender, receiver, message);
    }


    @Test
    public void GetMessage()  {
        assertNotNull(message1.getMessage());
        assertEquals(message, message1.getMessage());
    }

    @Test
    public void GetSender()  {
        assertNotNull(message1.getSender());
        assertEquals(sender, message1.getSender());
    }

    @Test
    public void GetReceiver()  {
        assertNotNull(message1.getReceiver());
        assertEquals(receiver, message1.getReceiver());
    }

//    @Test
//    public void GetID()  {
//        assertNotNull(message1.getId());
//    }

//    @Test
//    public void GetTimestamp()  {
//        assertNotNull(message1.getTimestamp());
//    }

//    @Test
//    public void GetRead()  {
//        assertNotNull(message1.isRead());
//    }
}





