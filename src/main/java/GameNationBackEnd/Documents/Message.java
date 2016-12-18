package GameNationBackEnd.Documents;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Document
public class Message {
    @Id
    private String id;

    @DBRef
    private User sender;

    @DBRef
    private User receiver;

    private String message;
    private Boolean read;
    private Date timestamp;

    public Message(User sender, User receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
        this.read = false;
        this.timestamp = new Date();
    }

    public String getId() {
        return id;
    }

    public User getSender() {
        return sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public String getMessage() {
        return message;
    }

    public Boolean isRead() {
        return read;
    }

    public Date getTimestamp() {
        return timestamp;
    }
}
