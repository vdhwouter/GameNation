package GameNationBackEnd.WebSockets;

import com.google.gson.Gson;
import GameNationBackEnd.Documents.User;
import GameNationBackEnd.Documents.Message;
import GameNationBackEnd.Repositories.UserRepository;
import GameNationBackEnd.Repositories.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.awt.print.PageFormat;
import java.awt.print.Pageable;
import java.awt.print.Printable;
import java.util.*;
import java.util.stream.Collectors;

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
        User user = userDB.findByUsernameIgnoreCase(session.getPrincipal().getName());
        sessions.put(user.getId(), session);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        Payload payload = gson.fromJson(message.getPayload(), Payload.class);

        switch (payload.getOp()) {
            case "chat": {
                ChatPayload chatPayload = gson.fromJson(payload.getD(), ChatPayload.class);

                Message msg = new Message(userDB.findOne(chatPayload.getSender()),
                        userDB.findOne(chatPayload.getReceiver()),
                        chatPayload.getMessage());

                msgDB.save(msg);

                List<ChatPayload> message_list = new ArrayList<>();
                message_list.add(chatPayload);
                String messages = gson.toJson(message_list);

                WebSocketSession s = sessions.get(chatPayload.getReceiver());
                if (s != null)
                    s.sendMessage(new TextMessage(messages));
            } break;

            case "read": {
                ChatPayload chatPayload = gson.fromJson(payload.getD(), ChatPayload.class);

                Message msg = new Message(userDB.findOne(chatPayload.getSender()),
                        userDB.findOne(chatPayload.getReceiver()),
                        chatPayload.getMessage());

                msgDB.save(msg);

                msgDB.findBySenderAndReceiver(userDB.findOne(chatPayload.getSender()), userDB.findOne(chatPayload.getReceiver()))
                        .stream().filter(m -> !m.isRead()).forEach(m -> m.read());
            } break;
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception { }
}
