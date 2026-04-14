import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.function.Consumer;

import javafx.application.Platform;
import javafx.scene.control.ListView;

public class Server{
    ListView<String> listItems;
    int count = 1;
    ArrayList<ClientThread> clients = new ArrayList<ClientThread>();;
    HashSet<String> usernames = new HashSet<String>();
    TheServer server;
    private Consumer<Message> callback;


    Server(Consumer<Message> call, ListView<String> items){

        callback = call;
        listItems = items;
        server = new TheServer();
        server.start();
    }

    public class TheServer extends Thread{

        public void run() {

            try(ServerSocket mysocket = new ServerSocket(5555);){
                System.out.println("Server is waiting for a client!");


                while(true) {

                    ClientThread c = new ClientThread(mysocket.accept(), count);
                    c.start();

                    count++;

                }
            }//end of try
            catch(Exception e) {
                callback.accept(new Message("Server did not launch"));
            }
        }//end of while
    }


    class ClientThread extends Thread{


        Socket connection;
        int count;
        String username;
        ObjectInputStream in;
        ObjectOutputStream out;


        ClientThread(Socket s, int count){
            this.connection = s;
            this.count = count;
        }

        public void sendMessage(Message message) {
            try {
                out.writeObject(message);
            } catch (Exception e) {
                System.err.println("Error sending message to " + username);
                e.printStackTrace();
            }
        }

        public void updateClients(Message message) {
            switch(message.type) {
                case TEXT:
                    for (ClientThread t : clients) {
                        if ( "Everyone".equals(message.recipient) ||
                                message.recipient.equals(t.username)) {
                            try {
                                t.out.writeObject(message);
                            } catch (Exception e) {
                                System.err.println("New User Error");
                            }
                        }
                    }
                    break;
                case NEWUSER:
                    for (ClientThread t : clients) {
                        try {
                            t.out.writeObject(message);
                        } catch (Exception e) {
                            System.err.println("New User Error");
                        }
                    }
                    break;
                case DISCONNECT:
                    for (ClientThread t : clients) {
                        try {
                            t.out.writeObject(message);
                        } catch (Exception e) {
                            System.err.println("New User Error");
                        }
                    }
                    break;
            }

        }

        public void messageToServer(String user, String mess) {
            Platform.runLater(() -> {
                listItems.getItems().add(user + ": " + mess);
            });
        }

        public void run(){

            try {
                in = new ObjectInputStream(connection.getInputStream());
                out = new ObjectOutputStream(connection.getOutputStream());
                connection.setTcpNoDelay(true);
            }
            catch(Exception e) {
                System.out.println("Streams not open");
            }

            while(username == null) {
                try {
                    Message attempt = (Message) in.readObject();
                    if (attempt.type == MessageType.NEWUSER) {
                        String wantedName = attempt.username;

                        synchronized (usernames) {
                            if (!usernames.contains(wantedName)) {
                                usernames.add(wantedName);
                                this.username = wantedName;
                                username = wantedName;

                                clients.add(this);

                                callback.accept(new Message(username, "Everyone", true));


                                for (ClientThread t : clients) {
                                    if (!t.username.equals(this.username)) {
                                        out.writeObject(new Message(t.username, "Everyone", true));
                                    }
                                }

                                updateClients(new Message(username, "Everyone", true));
                            } else {
                                Message taken = new Message("Everyone", true);
                                out.writeObject(taken);
                            }
                        }
                    }
                } catch(Exception e) {
                    System.out.println("Login error");
                    return;
                }

            }

            while(true) {
                try {
                    Message data = (Message) in.readObject();

                    System.out.println(
                            "FROM: " + data.username +
                                    " TO: " + data.recipient +
                                    " TYPE: " + data.type
                    );

                    callback.accept(data);

                    updateClients(data);

                }
                catch(Exception e) {
                    e.printStackTrace();
                    Message discon = new Message(username, "Everyone", false);
                    callback.accept(discon);
                    updateClients(discon);

                    synchronized(usernames) {
                        usernames.remove(username);
                    }

                    clients.remove(this);
                    break;
                }
            }
        }//end of run


    }//end of client thread
}