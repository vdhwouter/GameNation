package GameNationBackEnd.WebSockets;

/**
 * Created by Kjell on 12/17/2016.
 */
public class ChatPayload {
    private String sender;
    private String receiver;
    private String message;

    public ChatPayload(String sender, String receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    public String getSender() {
        return sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }
}
