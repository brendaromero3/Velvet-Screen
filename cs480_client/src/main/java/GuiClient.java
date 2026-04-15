import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.Region;
import javafx.geometry.Pos;
import java.sql.*;

public class GuiClient extends Application{

    String name;
    TextField enterMessage, usernameText;
    Button send, login;
    Label serverMessages, messaging, loginLabel;
    VBox usernameLogin;

    Scene loginScene, messagingScene;

    Client clientConnection;

    ComboBox<String> listUsers;
    ListView<String> listItems;
    ListView<String> messageLogs = new ListView<String>();
    public static String username;

    Connection conn;

    private void loginSceneSetup(Stage primaryStage) {
        usernameText = new TextField();
        login = new Button("Login");

        login.setOnAction(e -> {
            username = usernameText.getText().trim();
            if (!username.isEmpty()) {
                connectClient(username, primaryStage);
            }
        });

        loginLabel = new Label("Enter Username: ");
        usernameLogin = new VBox(loginLabel, usernameText, login);
        usernameLogin.setAlignment(Pos.CENTER);

        usernameLogin.setStyle("-fx-background-color: powderblue;");
        loginScene = new Scene(usernameLogin, 700, 550);
    }

    private void messageButtons() {
        send.setOnAction(e -> {
            String text = enterMessage.getText();

            String recipient = listUsers.getValue();
            if (recipient == null) {
                recipient = "Everyone";
            }

            if (!text.isEmpty()) {
                clientConnection.send(new Message(username, recipient, text));
            }
            messageLogs.getItems().add(username + ": " + enterMessage.getText());
            enterMessage.clear();
        });
    }
    private void messagingSceneSetup() {
        messaging = new Label("Messaging: ");
        send = new Button("Send Message");
        enterMessage = new TextField();
        enterMessage.setPromptText("Enter Message");
        Region space = new Region();
        space.setPrefWidth(170);

        HBox topLine = new HBox(space, messaging, listUsers);
        HBox sendMessageBox = new HBox(enterMessage, send);
        VBox scene = new VBox(topLine, messageLogs, sendMessageBox);
        sendMessageBox.setAlignment(Pos.CENTER);

        messageButtons();
        scene.setStyle("-fx-background-color: powderblue;");
        messagingScene = new Scene(scene, 700, 550);
    }

    public void updateServerMessages(String message) {
        Platform.runLater(()->{
            serverMessages.setText(message);
        });
    }
    private void connectClient(String username, Stage stage) {
        serverMessages = new Label("Waiting for messages...");
        messagingSceneSetup();

        clientConnection = new Client(data->{
            Platform.runLater(()->{
                switch (data.type) {
                    case NEWUSER:
                        listUsers.getItems().add(data.username);
                        listItems.getItems().add(data.username + " has joined!");
                        if (data.username.equals(username)) {
                            stage.setScene(messagingScene);
                            stage.setTitle("Client: " + username);
                        }
                        break;
                    case TAKEN:
                        loginLabel.setText("Username is taken. Please try another one.");
                        usernameText.clear();
                        break;
                    case DISCONNECT:
                        listUsers.getItems().remove(data.username);
                        listItems.getItems().add(data.username + " has disconnected!");
                        break;
                    case TEXT:
                        messageLogs.getItems().add(data.username + ": " + data.message);
                        updateServerMessages(data.message);
                        break;
                }
            });
        }, username, serverMessages);

        clientConnection.start();
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {

        listUsers = new ComboBox<String>();
        listUsers.getItems().add("Everyone");
        listUsers.setValue("Everyone");
        listItems = new ListView<String>();

        loginSceneSetup(primaryStage);

        primaryStage.setTitle("Login");
        primaryStage.setScene(loginScene);

        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.show();

    }

    // Connecting to the database
    private void connect () {
        try{
            // Replace "your_password" with your postgres password
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "postgres", "your_password");
            conn.setAutoCommit(false); // Make it so that it does not auto commit
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Checking if a user is an admin
    public boolean isAdmin (int id) {
        String query = "SELECT * FROM administrators WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, String.valueOf(id));  // Set parameter value using id as the placeholder
            ResultSet rs = stmt.executeQuery(); // Execute the query

            return rs.next();   // If a user is an admin, rs.next() returned true, otherwise false
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return false;
    }

    // Creating a function for users to log in
    public void LogIn (String username, String password) {
        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameter values using username and password as placeholders
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and get results
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                // Username and password combo exists!
                // Check if the user is an admin
                if (isAdmin(rs.getInt("user_id"))) {
                    // MOVE TO ADMIN SCREEN
                }
                else {
                    // MOVE TO USER SCREEN
                }
            }
            else{
                // Username and password combo does NOT exist!
                // Prompt user to enter another user and password or create an account
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
