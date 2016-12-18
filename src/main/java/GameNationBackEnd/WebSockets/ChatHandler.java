package GameNationBackEnd.WebSockets;

import com.google.gson.Gson;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.Message;
import GameNationBackEnd.Repositories.UserRepository;
import GameNationBackEnd.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.HashMap;

@Service
public class ChatHandler extends TextWebSocketHandler{
    @Autowired
    private UserRepository userDB;

    @Autowired
    private MessageRepository msgDB;

    private Gson gson = new Gson();
    private Map<String, WebSocketSession> sessions = new HashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        User user = userDB.findByUsername(session.getPrincipal().getName());
        sessions.put(user.getId(), session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ChatPayload chatPayload = gson.fromJson(message.getPayload(), ChatPayload.class);

        Message msg = new Message(userDB.findOne(chatPayload.getSender()),
                                  userDB.findOne(chatPayload.getReceiver()),
                                  chatPayload.getMessage());

        msgDB.save(msg);

        WebSocketSession s = sessions.get(chatPayload.getReceiver());
        if (s != null)
            s.sendMessage(new TextMessage(message.getPayload()));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception { }
}
