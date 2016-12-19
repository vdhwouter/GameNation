package GameNationBackEnd.WebSockets;

public class Payload {
    private String op;
    private String d;

    public Payload(String op, String d) {
        this.op = op;
        this.d = d;
    }

    public String getOp() {
        return op;
    }

    public String getD() {
        return d;
    }
}
