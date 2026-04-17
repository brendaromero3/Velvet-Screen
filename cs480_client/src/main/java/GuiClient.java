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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
    int user_id, membership_id;

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
    private boolean isAdmin (int id) {
        String query = "SELECT * FROM administrators WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);  // Set parameter value using id as the placeholder
            ResultSet rs = stmt.executeQuery(); // Execute the query

            return rs.next();   // If a user is an admin, rs.next() returned true, otherwise false
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

        return false;
    }

    // Getting and setting the membership id
    private void getMembershipId (int id) {
        String query = "SELECT * FROM membership WHERE user_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);  // Set parameter value using id as the placeholder
            ResultSet rs = stmt.executeQuery(); // Execute the query

            if(rs.next()){  // User has a membership
                membership_id = rs.getInt("membership_id");
            }
            else{
                membership_id = -1;
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Creating a function for users to log in
    private void LogIn (String username, String password) {
        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            // Set parameter values using username and password as placeholders
            stmt.setString(1, username);
            stmt.setString(2, password);

            // Execute the query and get results
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){  // Username and password combo exists!
                user_id = rs.getInt("user_id");
                // Check if the user is an admin
                if (isAdmin(user_id)) {
                    // MOVE TO ADMIN SCREEN
                }
                else {
                    getMembershipId(user_id);
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

    // Creating a function to get movie information based on a users input
    private void getinfo (String movieName, String genre, String date, String time) {
        String query = "SELECT * FROM movies m " +
                "JOIN movies_genre mg ON m.movie_id = mg.movie_id " +
                "JOIN screening s ON m.movie_id = s.movie_id " +
                "JOIN theater t ON s.theater_id = t.theater_id " +
                "WHERE m.movie_id > 0";   // Adding this constraint that is always true so that we can add more constraints

        List<String>  params = new ArrayList<>();

        if (movieName != null && !movieName.isBlank()) {
            query += " AND m.title LIKE ?";     // Adding the constraint to the query
            params.add("%" + movieName + "%");  // Adding the movie name to the parameter list with % so that it finds similar movie titles
        }

        if (genre != null && !genre.isBlank()) {
            query += " AND mg.genre = ?";     // Adding the constraint to the query
            params.add(genre);  // Adding the genre to the parameter list
        }

        if (date != null && !date.isBlank()) {
            query += " AND s.date_time::DATE = ?";     // Adding the constraint to the query
            params.add(date);  // Adding the date to the parameter list
        }

        if (time != null && !time.isBlank()) {
            query += " AND s.date_time::TIME >= ?";     // Adding the constraint to the query
            params.add(time);  // Adding the time to the parameter list
        }

        query += ";";   // adding a semicolon to the end of the query

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i+1, params.get(i));  // Set parameter value using params as the placeholder
            }
            ResultSet rs = stmt.executeQuery(); // Execute the query

            while (rs.next()) {
                // Iterate over results
            }
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void addPaymentMethod(String card_num, String billing_addr) {
        String query = "INSERT INTO card_info VALUES (?,?);";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);  // Set parameter value using card_num as the placeholder
            stmt.setInt(2, user_id);  // Set parameter value using user_id as the placeholder
            stmt.executeUpdate(); // Execute the query
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "INSERT INTO credit VALUES (?,?);";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, card_num);  // Set parameter value using card_num as the placeholder
                stmt2.setString(2, billing_addr);  // Set parameter value using billing_addr as the placeholder
                stmt2.executeUpdate(); // Execute the query
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        try {
            conn.commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void deletePaymentMethods (String card_num, String billing_addr) {
        // Deleting the billing address first if it exists
        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "DELETE FROM credit WHERE card_number = ? AND billing_address = ?;";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, card_num);  // Set parameter value using card_num as the placeholder
                stmt2.setString(2, billing_addr);  // Set parameter value using billing_addr as the placeholder
                stmt2.executeUpdate(); // Execute the query
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        String query = "DELETE FROM card_info WHERE card_number = ? AND user_id = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);  // Set parameter value using card_num as the placeholder
            stmt.setInt(2, user_id);  // Set parameter value using user_id as the placeholder
            stmt.executeUpdate(); // Execute the query
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }

        try {
            conn.commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    private void updatePaymentMethods (String card_num, String billing_addr, String old_billing_addr) {
        String query = "UPDATE card_info SET card_number = ? WHERE user_id = ? AND card_number = ?;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);  // Set parameter value using card_num as the placeholder
            stmt.setInt(2, user_id);  // Set parameter value using user_id as the placeholder
            stmt.setString(3, old_billing_addr);  // Set parameter value using old_billing_addr as the placeholder
            stmt.executeUpdate(); // Execute the query
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "UPDATE credit SET billing_address = ? WHERE card_number = ?;";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, billing_addr);  // Set parameter value using card_num as the placeholder
                stmt2.setString(2, card_num);  // Set parameter value using billing_addr as the placeholder
                stmt2.executeUpdate(); // Execute the query
            }
            catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        try {
            conn.commit();
        }
        catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

}
