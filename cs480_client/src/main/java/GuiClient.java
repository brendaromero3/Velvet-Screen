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
import javafx.scene.text.Font;

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
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-Italic-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/Questrial-Regular.ttf"), 14);

        connect();

        Runnable[] goToLanding  = new Runnable[1];
        Runnable[] goToLogin    = new Runnable[1];
        Runnable[] goToRegister = new Runnable[1];

        goToLanding[0] = () -> {
            primaryStage.setScene(new LandingScreen(primaryStage, goToLogin[0], goToRegister[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToLogin[0] = () -> {
            primaryStage.setScene(new LoginScreen(primaryStage, this, goToLanding[0], goToRegister[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToRegister[0] = () -> {
            primaryStage.setScene(new RegisterScreen(primaryStage, this, goToLanding[0], goToLogin[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        LandingScreen landing = new LandingScreen(primaryStage, goToLogin[0], goToRegister[0]);
        primaryStage.setTitle("Velvet Screen");
        primaryStage.setScene(landing.getScene());
        primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        primaryStage.show();
    }

    // Connecting to the database
    private void connect () {
        try{
            // Replace "your_password" with your postgres password
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "lena", "");
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
    public boolean logIn(String email, String password, Stage stage) {
        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user_id = rs.getInt("user_id");
                if (isAdmin(user_id)) {
                    // TODO: go to admin dashboard
                    System.out.println("Admin logged in!");
                } else {
                    getMembershipId(user_id);
                    // TODO: go to client dashboard
                    System.out.println("Client logged in!");
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return false;
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

    // registers by adding data to fields!
    public boolean register(String name, String email, String address, String password, String interests, boolean isAdmin) {
        try {
            // get next user id
            int newUserId;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(user_id), 0) + 1 FROM users")) {
                ResultSet rs = stmt.executeQuery();
                rs.next();
                newUserId = rs.getInt(1);
            }

            // insert into users
            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (user_id, email_address, password) VALUES (?, ?, ?)")) {
                stmt.setInt(1, newUserId);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.executeUpdate();
            }

            if (isAdmin) {
                // insert into administrators
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO administrators (user_id) VALUES (?)")) {
                    stmt.setInt(1, newUserId);
                    stmt.executeUpdate();
                }
            } else {
                // insert into clients
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO clients (user_id, name, address, interests) VALUES (?, ?, ?, ?)")) {
                    stmt.setInt(1, newUserId);
                    stmt.setString(2, name);
                    stmt.setString(3, address);
                    stmt.setString(4, interests);
                    stmt.executeUpdate();
                }

                // create membership
                int newMembershipId;
                try (PreparedStatement stmt = conn.prepareStatement(
                        "SELECT COALESCE(MAX(membership_id), 0) + 1 FROM membership")) {
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    newMembershipId = rs.getInt(1);
                }
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO membership (membership_id, user_id, movies_watched, num_free_earned) VALUES (?, ?, 0, 0)")) {
                    stmt.setInt(1, newMembershipId);
                    stmt.setInt(2, newUserId);
                    stmt.executeUpdate();
                }
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            try { conn.rollback(); } catch (Exception ex) { }
            return false;
        }
    }

}
