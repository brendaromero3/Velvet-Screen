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
import javafx.scene.text.Font;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GuiClient extends Application {

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

    // Navigation runnables accessible across the app
    public Runnable goToLanding;
    public Runnable goToLogin;
    public Runnable goToRegister;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load fonts
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-Italic-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/Questrial-Regular.ttf"), 14);

        connect();

        Runnable[] goToLandingArr  = new Runnable[1];
        Runnable[] goToLoginArr    = new Runnable[1];
        Runnable[] goToRegisterArr = new Runnable[1];

        goToLandingArr[0] = () -> {
            primaryStage.setScene(new LandingScreen(primaryStage, goToLoginArr[0], goToRegisterArr[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToLoginArr[0] = () -> {
            primaryStage.setScene(new LoginScreen(primaryStage, this, goToLandingArr[0], goToRegisterArr[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToRegisterArr[0] = () -> {
            primaryStage.setScene(new RegisterScreen(primaryStage, this, goToLandingArr[0], goToLoginArr[0]).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        // Store as fields so logIn() can use them
        goToLanding  = goToLandingArr[0];
        goToLogin    = goToLoginArr[0];
        goToRegister = goToRegisterArr[0];

        primaryStage.setTitle("Velvet Screen");
        primaryStage.setScene(new LandingScreen(primaryStage, goToLogin, goToRegister).getScene());
        primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        primaryStage.show();
    }

    // Connecting to the database
    private void connect() {
        try {
            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "postgres", "your_password");
            conn.setAutoCommit(false);
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Checking if a user is an admin
    private boolean isAdmin(int id) {
        String query = "SELECT * FROM administrators WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            return rs.next();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return false;
    }

    // Getting and setting the membership id
    private void getMembershipId(int id) {
        String query = "SELECT * FROM membership WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                membership_id = rs.getInt("membership_id");
            } else {
                membership_id = -1;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Get client name
    public String getClientName() {
        String query = "SELECT name FROM clients WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("name");
            }
        } catch (Exception e) {
            System.out.println("Error getting client name: " + e.getMessage());
        }
        return "Guest";
    }

    // Get movies watched for rewards
    public int getMoviesWatched() {
        String query = "SELECT movies_watched FROM membership WHERE user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("movies_watched");
            }
        } catch (Exception e) {
            System.out.println("Error getting movies watched: " + e.getMessage());
        }
        return 0;
    }

    // Login
    public boolean logIn(String email, String password, Stage stage) {
        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                user_id = rs.getInt("user_id");
                if (isAdmin(user_id)) {
                    System.out.println("Admin logged in!");
                    // TODO: go to admin dashboard
                } else {
                    getMembershipId(user_id);
                    Platform.runLater(() -> {
                        ClientDashboard dashboard = new ClientDashboard(
                            stage, this, goToLanding,
                            (searchTerm) -> {
                                stage.setScene(new BrowseScreen(
                                    stage, this, searchTerm,
                                    () -> {
                                        ClientDashboard db = new ClientDashboard(stage, this, goToLanding,
                                            (s) -> {
                                                stage.setScene(new BrowseScreen(stage, this, s,
                                                    null, goToLanding, null, null, null).getScene());
                                                stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                                            },
                                            () -> System.out.println("Go to account"),
                                            () -> System.out.println("Go to payments")
                                        );
                                        stage.setScene(db.getScene());
                                        stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                                    },
                                    goToLanding,
                                    () -> System.out.println("Go to account"),
                                    () -> System.out.println("Go to payments"),
                                    (movie) -> System.out.println("View details: " + movie[1])
                                ).getScene());
                                stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                            },
                            () -> System.out.println("Go to account"),
                            () -> System.out.println("Go to payments")
                        );
                        stage.setScene(dashboard.getScene());
                        stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                    });
                }
                return true;
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return false;
    }

    // Register
    public boolean register(String name, String email, String address, String password, String interests, boolean isAdmin) {
        try {
            int newUserId;
            try (PreparedStatement stmt = conn.prepareStatement("SELECT COALESCE(MAX(user_id), 0) + 1 FROM users")) {
                ResultSet rs = stmt.executeQuery();
                rs.next();
                newUserId = rs.getInt(1);
            }

            try (PreparedStatement stmt = conn.prepareStatement(
                    "INSERT INTO users (user_id, email_address, password) VALUES (?, ?, ?)")) {
                stmt.setInt(1, newUserId);
                stmt.setString(2, email);
                stmt.setString(3, password);
                stmt.executeUpdate();
            }

            if (isAdmin) {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO administrators (user_id) VALUES (?)")) {
                    stmt.setInt(1, newUserId);
                    stmt.executeUpdate();
                }
            } else {
                try (PreparedStatement stmt = conn.prepareStatement(
                        "INSERT INTO clients (user_id, name, address, interests) VALUES (?, ?, ?, ?)")) {
                    stmt.setInt(1, newUserId);
                    stmt.setString(2, name);
                    stmt.setString(3, address);
                    stmt.setString(4, interests);
                    stmt.executeUpdate();
                }

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

    // Get movie info based on search filters
    public List<String[]> getMovies(String movieName, String language, String genre, String date, String timeStart, String timeEnd) {
        List<String[]> results = new ArrayList<>();

        String query = "SELECT DISTINCT m.movie_id, m.title, m.original_language, " +
                "m.release_date, m.major_studio, s.screening_id, s.date_time, " +
                "t.theater_id, t.max_occupancy, t.is_3d, t.fancy_sound, " +
                "(t.max_occupancy - COALESCE(" +
                "  (SELECT SUM(num_tickets) FROM ticket_sale WHERE screening_id = s.screening_id), 0" +
                ")) AS seats_available " +
                "FROM movies m " +
                "JOIN screening s ON m.movie_id = s.movie_id " +
                "JOIN theater t ON s.theater_id = t.theater_id " +
                "WHERE m.movie_id > 0";

        List<String> params = new ArrayList<>();

        if (movieName != null && !movieName.isBlank()) {
            query += " AND LOWER(m.title) LIKE LOWER(?)";
            params.add("%" + movieName + "%");
        }
        if (language != null && !language.isBlank()) {
            query += " AND LOWER(m.original_language) = LOWER(?)";
            params.add(language);
        }
        if (genre != null && !genre.isBlank()) {
            query += " AND EXISTS (SELECT 1 FROM movies_genre mg WHERE mg.movie_id = m.movie_id AND mg.genre = ?)";
            params.add(genre);
        }
        if (date != null && !date.isBlank()) {
            query += " AND s.date_time::DATE = ?";
            params.add(date);
        }
        if (timeStart != null && timeEnd != null) {
            query += " AND s.date_time::TIME BETWEEN ?::TIME AND ?::TIME";
            params.add(timeStart);
            params.add(timeEnd);
        }

        query += " ORDER BY s.date_time;";

        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            for (int i = 0; i < params.size(); i++) {
                stmt.setString(i + 1, params.get(i));
            }
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                results.add(new String[]{
                    rs.getString("movie_id"),
                    rs.getString("title"),
                    rs.getString("original_language"),
                    rs.getString("release_date"),
                    rs.getString("major_studio"),
                    rs.getString("screening_id"),
                    rs.getString("date_time"),
                    rs.getString("theater_id"),
                    rs.getString("max_occupancy"),
                    rs.getString("is_3d"),
                    rs.getString("fancy_sound"),
                    rs.getString("seats_available")
                });
            }
        } catch (Exception e) {
            System.out.println("Error getting movies: " + e.getMessage());
        }

        return results;
    }

    // Add payment method
    private void addPaymentMethod(String card_num, String billing_addr) {
        String query = "INSERT INTO card_info VALUES (?,?);";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "INSERT INTO credit VALUES (?,?);";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, card_num);
                stmt2.setString(2, billing_addr);
                stmt2.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        try { conn.commit(); } catch (Exception e) { System.out.println("Error: " + e); }
    }

    // Delete payment method
    private void deletePaymentMethods(String card_num, String billing_addr) {
        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "DELETE FROM credit WHERE card_number = ? AND billing_address = ?;";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, card_num);
                stmt2.setString(2, billing_addr);
                stmt2.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        String query = "DELETE FROM card_info WHERE card_number = ? AND user_id = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);
            stmt.setInt(2, user_id);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        try { conn.commit(); } catch (Exception e) { System.out.println("Error: " + e); }
    }

    // Update payment method
    private void updatePaymentMethods(String card_num, String billing_addr, String old_billing_addr) {
        String query = "UPDATE card_info SET card_number = ? WHERE user_id = ? AND card_number = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, card_num);
            stmt.setInt(2, user_id);
            stmt.setString(3, old_billing_addr);
            stmt.executeUpdate();
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }

        if (billing_addr != null && !billing_addr.isBlank()) {
            String query2 = "UPDATE credit SET billing_address = ? WHERE card_number = ?;";
            try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                stmt2.setString(1, billing_addr);
                stmt2.setString(2, card_num);
                stmt2.executeUpdate();
            } catch (Exception e) {
                System.out.println("Error: " + e);
            }
        }

        try { conn.commit(); } catch (Exception e) { System.out.println("Error: " + e); }
    }
}