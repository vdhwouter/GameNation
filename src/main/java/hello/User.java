package hello;

import org.springframework.data.annotation.Id;

public class User {
    @Id
    public String id;

    public String name;
    public String data;

    public User(String name, String data) {
        this.name = name;
        this.data = data;
    }
}
