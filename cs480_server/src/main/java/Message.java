import java.io.Serializable;

public class Message implements Serializable {
    static final long serialVersionUID = 42L;
    MessageType type;
    String message, recipient, username;

    public Message(String user, String i, boolean connect) {
        if (connect) {
            type = MessageType.NEWUSER;
            message = "User " + user + " has joined!";
            recipient = i;
            username = user;
        } else {
            type = MessageType.DISCONNECT;
            message = "User " + user + " has disconnected!";
            recipient = i;
            username = user;
        }
    }

    public Message(String mess) {
        type = MessageType.TEXT;
        message = mess;
        recipient = "Everyone";
    }

    public Message(String rec, boolean taken) {
        type = MessageType.TAKEN;
        message = "Username already taken";
        recipient = rec;
    }

    public Message(String user, String rec, String mess) {
        type = MessageType.TEXT;
        message = mess;
        recipient = rec;
        username = user;
    }
}

