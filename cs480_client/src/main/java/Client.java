import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.function.Consumer;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;


public class Client extends Thread{


    Socket socketClient;

    ObjectOutputStream out;
    ObjectInputStream in;

    private Consumer<Message> callback;
    String username;

    Label serverMessages;

    Client(Consumer<Message> call, String user, Label messages){

        callback = call;
        username = user;
        serverMessages = messages;
    }

    public void run() {

        try {
            socketClient= new Socket("127.0.0.1",5555);
            out = new ObjectOutputStream(socketClient.getOutputStream());
            in = new ObjectInputStream(socketClient.getInputStream());
            socketClient.setTcpNoDelay(true);

            out.writeObject(new Message(username, "Everyone", true));
        }
        catch(Exception e) {}

        while(true) {

            try {
                Message message = (Message) in.readObject();

                callback.accept(message);
            }
            catch(Exception e) {}
        }

    }

    public void send(Message data) {

        try {
            out.writeObject(data);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            out.close();
            in.close();
            socketClient.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}