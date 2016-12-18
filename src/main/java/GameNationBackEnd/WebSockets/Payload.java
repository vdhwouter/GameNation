package GameNationBackEnd.WebSockets;

public class Payload {
    private String type;
    private String data;

    public Payload(String type, String data) {
        this.type = type;
        this.data = data;
    }

    public String getType() {
        return type;
    }

    public String getData() {
        return data;
    }
}
