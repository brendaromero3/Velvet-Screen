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
import java.util.function.Consumer;

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

//    Connection conn;
    int user_id, membership_id;

    // Navigation runnables accessible across the app
    public Runnable goToLanding;
    public Runnable goToLogin;
    public Runnable goToRegister;
    public Runnable goToBrowse;
    public Runnable goToPayments;
    public Runnable goToAccount;
    public Runnable goToDashboard;

    public Consumer<String[]> goToMovieDetail;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/NotoSerifDisplay-Italic-VariableFont_wdth,wght.ttf"), 14);
        Font.loadFont(getClass().getResourceAsStream("/Questrial-Regular.ttf"), 14);

//        connect();

        Runnable[] goToLandingArr  = new Runnable[1];
        Runnable[] goToLoginArr    = new Runnable[1];
        Runnable[] goToRegisterArr = new Runnable[1];
        Runnable[] goToBrowseArr   = new Runnable[1];
        Runnable[] goToPaymentsArr = new Runnable[1];
        Runnable[] goToAccountArr = new Runnable[1];
        Runnable[] goToDashboardArr = new Runnable[1];

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

        goToBrowseArr[0] = () -> {
            primaryStage.setScene(new BrowseScreen(primaryStage, this, null, goToDashboardArr[0], goToLoginArr[0], goToAccountArr[0], goToPaymentsArr[0], goToMovieDetail).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToMovieDetail = (movie) -> {
            primaryStage.setScene(new MovieDetailScreen(primaryStage, this, goToLandingArr[0], goToBrowseArr[0], goToLoginArr[0], goToAccountArr[0], goToPaymentsArr[0], movie).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToDashboardArr[0] = () -> {
            primaryStage.setScene(new ClientDashboard(primaryStage, this, goToLanding, (s) -> {
                primaryStage.setScene(new BrowseScreen(primaryStage, this, s,
                        goToDashboard, goToLanding, null, goToPayments, goToMovieDetail).getScene());
                primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
            }, goToAccount, goToPayments).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToPaymentsArr[0] = () -> {
            primaryStage.setScene(new PaymentMethodsScreen(
                primaryStage, this,
                goToDashboardArr[0],
                goToLandingArr[0],
                goToBrowseArr[0],
                goToAccountArr[0]
            ).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToAccountArr[0] = () -> {
            primaryStage.setScene(new ClientProfileScreen(
                primaryStage, this,
                goToDashboardArr[0],
                goToLandingArr[0],
                goToBrowseArr[0],
                goToPaymentsArr[0]
            ).getScene());
            primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        };

        goToLanding  = goToLandingArr[0];
        goToLogin    = goToLoginArr[0];
        goToRegister = goToRegisterArr[0];
        goToBrowse   = goToBrowseArr[0];
        goToPayments = goToPaymentsArr[0];
        goToAccount = goToAccountArr[0];
        goToDashboard = goToDashboardArr[0];

        primaryStage.setTitle("Velvet Screen");
        primaryStage.setScene(new LandingScreen(primaryStage, goToLogin, goToRegister).getScene());
        primaryStage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        primaryStage.show();
    }

    private Connection getConnection() throws SQLException {
//        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "lena", "");
        return DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "postgres", "3D33Jsski2s");
    }

    // Connecting to the database
//    private void connect() {
//        try {
////            conn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/movie_ticketing", "lena", "");
//            conn.setAutoCommit(false);
//        } catch (Exception e) {
//            System.out.println("Error: " + e);
//        }
//    }

    // Checking if a user is an admin
    private boolean isAdmin(int id) {
        String query = "SELECT * FROM administrators WHERE user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try(ResultSet rs = stmt.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return false;
    }

    // Getting and setting the membership id
    private void getMembershipId(int id) {
        String query = "SELECT * FROM membership WHERE user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    membership_id = rs.getInt("membership_id");
                } else {
                    membership_id = -1;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
    }

    // Get client name
    public String getClientName() {
        String query = "SELECT name FROM clients WHERE user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getString("name");
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting client name: " + e.getMessage());
        }
        return "Guest";
    }

    // Get movies watched for rewards
    public int getMoviesWatched() {
        String query = "SELECT movies_watched FROM membership WHERE user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("movies_watched");
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting movies watched: " + e.getMessage());
        }
        return 0;
    }

    // Login
    public boolean logIn(String email, String password, Stage stage) {
        String query = "SELECT * FROM users WHERE email_address = ? AND password = ?;";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
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
                                                    goToDashboard, goToLanding, null, goToPayments, goToMovieDetail).getScene());
                                                stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                                            },
                                            goToAccount,
                                            goToPayments
                                        );
                                        stage.setScene(db.getScene());
                                        stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                                    },
                                    goToLanding,
                                    goToAccount,
                                    goToPayments,
                                    (movie) -> goToMovieDetail.accept(movie)
                                ).getScene());
                                stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                            },
                            goToAccount,
                            goToPayments
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
        String query = "SELECT COALESCE(MAX(user_id), 0) + 1 FROM users";
        String query1 = "INSERT INTO users (user_id, email_address, password) VALUES (?, ?, ?)";
        String query2 = "INSERT INTO administrators (user_id) VALUES (?)";
        String query3 =  "INSERT INTO clients (user_id, name, address, interests) VALUES (?, ?, ?, ?)";
        String query4 = "SELECT COALESCE(MAX(membership_id), 0) + 1 FROM membership";
        String query5 = "INSERT INTO membership (membership_id, user_id, movies_watched, num_free_earned) VALUES (?, ?, 0, 0)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                int newUserId;
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    newUserId = rs.getInt(1);
                }
                try (PreparedStatement stmt = conn.prepareStatement(query1)) {
                    stmt.setInt(1, newUserId);
                    stmt.setString(2, email);
                    stmt.setString(3, password);
                    stmt.executeUpdate();
                }

                if (isAdmin) {
                    try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                        stmt.setInt(1, newUserId);
                        stmt.executeUpdate();
                    }
                } else {
                    try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                        stmt.setInt(1, newUserId);
                        stmt.setString(2, name);
                        stmt.setString(3, address);
                        stmt.setString(4, interests);
                        stmt.executeUpdate();
                    }

                    int newMembershipId;
                    try (PreparedStatement stmt = conn.prepareStatement(query4)) {
                        ResultSet rs = stmt.executeQuery();
                        rs.next();
                        newMembershipId = rs.getInt(1);
                    }
                    try (PreparedStatement stmt = conn.prepareStatement(query5)) {
                        stmt.setInt(1, newMembershipId);
                        stmt.setInt(2, newUserId);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Registration Error: " + e);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return false;
        }
    }

    // Get movie info based on search filters
    public List<String[]> getMovies(boolean screenings, String movieName, String language, String genre, String date, String timeStart, String timeEnd) {
        List<String[]> results = new ArrayList<>();

        String query = "SELECT ";

        if (!screenings) {
            query += "DISTINCT ON (m.movie_id) ";   // Only adding this constraint when we don't need the exact screenings (for BrowseScreen)
        }

        query += "m.movie_id, m.title, m.original_language, " +
                "m.release_date, m.major_studio, s.screening_id, s.date_time, " +
                "t.theater_id, t.max_occupancy, t.is_3d, t.fancy_sound, " +
                "(t.max_occupancy - COALESCE(" +
                "  (SELECT SUM(num_tickets) FROM ticket_sale WHERE screening_id = s.screening_id), 0" +
                ")) AS seats_available, m.length " +
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
            query += " AND EXISTS (SELECT 1 FROM movies_genre mg WHERE mg.movie_id = m.movie_id AND LOWER(mg.genre) = LOWER(?))";
            params.add(genre);
        }
        if (date != null && !date.isBlank()) {
            query += " AND s.date_time::DATE = ?::DATE";
            params.add(date);
        }
        if (timeStart != null && timeEnd != null) {
            query += " AND s.date_time::TIME BETWEEN ?::TIME AND ?::TIME";
            params.add(timeStart);
            params.add(timeEnd);
        }

        query += " ORDER BY m.movie_id, s.date_time;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
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
                    rs.getString("seats_available"),
                    rs.getString("length")
                });
            }
        } catch (Exception e) {
            System.out.println("Error getting movies: " + e.getMessage());
        }

        return results;
    }

    // Get all Actors for a specific movie
    public List<String[]> getActors(String movieId) {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT p.name, a.character_name " +
                "FROM actors a " +
                "JOIN persons p on a.person_id = p.person_id " +
                "WHERE a.movie_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(movieId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new String[]{
                            rs.getString("name"),
                            rs.getString("character_name")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting actors: " + e.getMessage());
        }
        return results;
    }

    // Get all Directors for a specific movie
    public List<String[]> getDirectors(String movieId) {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT p.name " +
                "FROM directors d " +
                "JOIN persons p on d.person_id = p.person_id " +
                "WHERE d.movie_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(movieId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new String[]{ rs.getString("name") });
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting directors: " + e.getMessage());
        }
        return results;
    }

    // Get all Writers for a specific movie
    public List<String[]> getWriters(String movieId) {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT p.name " +
                "FROM writers w " +
                "JOIN persons p on w.person_id = p.person_id " +
                "WHERE w.movie_id = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(movieId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new String[]{ rs.getString("name") });
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting writers: " + e.getMessage());
        }
        return results;
    }

    // Get all Producers for a specific movie
    public List<String[]> getProducers(String movieId) {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT p.name " +
                "FROM producers pr " +
                "JOIN persons p on pr.person_id = p.person_id " +
                "WHERE pr.movie_id = ?";
        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, Integer.parseInt(movieId));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new String[]{ rs.getString("name") });
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting producers: " + e.getMessage());
        }
        return results;
    }

    // Add payment method
    public boolean addPaymentMethod(String card_num, String billing_addr) {
        String query = "INSERT INTO card_info VALUES (?,?);";
        String query2 = "INSERT INTO credit VALUES (?,?);";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, card_num);
                    stmt.setInt(2, user_id);
                    stmt.executeUpdate();
                }

                if (billing_addr != null && !billing_addr.isBlank()) {
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setString(1, card_num);
                        stmt2.setString(2, billing_addr);
                        stmt2.executeUpdate();
                    }
                }
                conn.commit();
                return true;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                    return false;
                }
            }
        } catch (Exception e) {
            System.out.println("Error: " + e);
        }
        return false;
    }

    // Delete payment method
    public boolean deletePaymentMethods(String card_num, String billing_addr) {
        String query2 = "DELETE FROM credit WHERE card_number = ? AND billing_address = ?;";
        String query = "DELETE FROM card_info WHERE card_number = ? AND user_id = ?;";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                if (billing_addr != null && !billing_addr.isBlank()) {
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setString(1, card_num);
                        stmt2.setString(2, billing_addr);
                        stmt2.executeUpdate();
                    }
                }

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, card_num);
                    stmt.setInt(2, user_id);
                    stmt.executeUpdate();
                }
                conn.commit();
                return true;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Error: " + e);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return false;
        }
    }

    // Update payment method
    public boolean updatePaymentMethods(String card_num, String billing_addr, String old_billing_addr) {
        String query = "UPDATE card_info SET card_number = ? WHERE user_id = ? AND card_number = ?;";
        String query2 = "UPDATE credit SET billing_address = ? WHERE card_number = ?;";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, card_num);
                    stmt.setInt(2, user_id);
                    stmt.setString(3, old_billing_addr);
                    stmt.executeUpdate();
                }

                if (billing_addr != null && !billing_addr.isBlank()) {
                    try (PreparedStatement stmt2 = conn.prepareStatement(query2)) {
                        stmt2.setString(1, billing_addr);
                        stmt2.setString(2, card_num);
                        stmt2.executeUpdate();
                    }
                }
                conn.commit();
                return true;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Error: " + e);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return false;
        }
    }

    // Schedule a movie screening
    public boolean schedule(String movieName, int theater_id, String date, String time) {
        int movie_id;
        int newScreeningId;
        String query = "SELECT movie_id FROM movies WHERE title = ?";
        String query2 = "SELECT COALESCE(MAX(screening_id), 0) + 1 FROM screening";
        String query3 = "INSERT INTO screening (screening_id, movie_id, theater_id, date_time) VALUES (?, ?, ?, ?::TIMESTAMP)";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, movieName);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    movie_id = rs.getInt("movie_id");
                }
                try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    newScreeningId = rs.getInt(1);
                }
                try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                    stmt.setInt(1, newScreeningId);
                    stmt.setInt(2, movie_id);
                    stmt.setInt(3, theater_id);
                    stmt.setString(4, (date + time));
                    stmt.executeUpdate();
                }
                conn.commit();
                return true;

            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Error: " + e);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return false;
        }
    }

    // Monitor movie screenings — returns remaining seat capacity
    public int monitor(String movieName, String date_time) {
        String query = "SELECT movie_id FROM movies WHERE title = ?";
        String query2 = "SELECT screening_id, theater_id FROM screening WHERE movie_id = ? AND date_time = ?::TIMESTAMP";
        String query3 = "SELECT max_occupancy FROM theater WHERE theater_id = ?";
        String query4 = "SELECT SUM(num_tickets) FROM ticket_sale WHERE screening_id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                int movie_id, screening_id, theater_id, max_capacity, sold;

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, movieName);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    movie_id = rs.getInt("movie_id");
                }

                try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                    stmt.setInt(1, movie_id);
                    stmt.setString(2, date_time); // fixed bug: was setString(1)
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    screening_id = rs.getInt("screening_id");
                    theater_id   = rs.getInt("theater_id");
                }

                try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                    stmt.setInt(1, theater_id);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    max_capacity = rs.getInt("max_occupancy");
                }

                try (PreparedStatement stmt = conn.prepareStatement(query4)) {
                    stmt.setInt(1, screening_id);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    sold = rs.getInt(1);
                }
                conn.commit();
                return max_capacity - sold;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Error: " + e);
                return -1;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return -1;
        }
    }

    // Get all payment methods for the logged in user
    public List<String[]> getPaymentMethods() {
        List<String[]> results = new ArrayList<>();
        String query = "SELECT ci.card_number, " +
                "CASE WHEN cr.card_number IS NOT NULL THEN 'Credit' ELSE 'Debit' END AS card_type, " +
                "cr.billing_address " +
                "FROM card_info ci " +
                "LEFT JOIN credit cr ON ci.card_number = cr.card_number " +
                "WHERE ci.user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new String[]{
                            rs.getString("card_number"),
                            rs.getString("card_type"),
                            rs.getString("billing_address")
                    });
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting payment methods: " + e.getMessage());
        }
        return results;
    }

    // Get client info for profile screen
    public String[] getClientInfo() {
        String query = "SELECT c.name, u.email_address, c.address, c.interests " +
                    "FROM clients c JOIN users u ON c.user_id = u.user_id " +
                    "WHERE c.user_id = ?;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, user_id);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new String[]{
                            rs.getString("name"),
                            rs.getString("email_address"),
                            rs.getString("address"),
                            rs.getString("interests")
                    };
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting client info: " + e.getMessage());
        }
        return new String[]{"", "", "", ""};
    }

    // Update client info
    public boolean updateClientInfo(String name, String email, String address, String interests) {
        String query = "UPDATE clients SET name = ?, address = ?, interests = ? WHERE user_id = ?";
        String query2 =  "UPDATE users SET email_address = ? WHERE user_id = ?";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setString(1, name);
                    stmt.setString(2, address);
                    stmt.setString(3, interests);
                    stmt.setInt(4, user_id);
                    stmt.executeUpdate();
                }
                try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                    stmt.setString(1, email);
                    stmt.setInt(2, user_id);
                    stmt.executeUpdate();
                }

                conn.commit();
                return true;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                System.out.println("Error: " + e);
                return false;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return false;
        }
    }

    // Change password
    public boolean changePassword(String currentPass, String newPass) {
        String query = "UPDATE users SET password = ? WHERE user_id = ? AND password = ?";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {
            conn.setAutoCommit(false);

            stmt.setString(1, newPass);
            stmt.setInt(2, user_id);
            stmt.setString(3, currentPass);

            int update = stmt.executeUpdate();

            if (update == 0) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                return false;
            }

            conn.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Error changing password: " + e.getMessage());
            return false;
        }
    }

    // Admin stats functions

    public static class ScreeningStat {
        int theater_id;
        int screening_id;
        String movie_name;
        double revenue;
        int seatsSold;
        int capacity;
    }

    public List<ScreeningStat> screeningStats() {
        List<ScreeningStat> results = new ArrayList<>();

        // revenue per screening - stat.revenue
        // percentage of seats sold per screening - seatsSold/capacity

        String query = "SELECT am.theater_id, s.screening_id, m.title, " +
                "COALESCE(SUM(ts.total_price), 0.00), COALESCE(SUM(ts.num_tickets), 0), " +
                "t.max_capacity FROM administrator_manages am " +
                "JOIN theater t ON am.theater_id = t.theater_id " +
                "JOIN screening s ON t.theater_id = s.theater_id " +
                "JOIN movies m ON s.movie_id = m.movie_id " +
                "JOIN ticket_sale ts ON s.screening_id = ts.screening_id " +
                "GROUP BY s.screening_id " +
                "ORDER BY t.theater_id;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    ScreeningStat stat = new ScreeningStat();
                    stat.theater_id = rs.getInt(1);
                    stat.screening_id = rs.getInt(2);
                    stat.movie_name = rs.getString(3);
                    stat.revenue = rs.getDouble(4);
                    stat.seatsSold = rs.getInt(5);
                    stat.capacity = rs.getInt(6);
                    results.add(stat);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting screening stats: " + e.getMessage());
        }
        return results;
    }

    public static class MovieStat {
        String movie_name;
        int theater_id;
        double revenue;
        int seatsSold;
    }

    public List<MovieStat> movieStats() {
        List<MovieStat> results = new ArrayList<>();

        // revenue per movie - stat.revenue
        // movie with most revenue  - manually find the movie with the highest revenue
        // total tickets sold per movie - stat.seatsSold

        String query = "SELECT m.title, t.theater_id, COALESCE(SUM(ts.total_price), 0.00), " +
                "COALESCE(SUM(ts.num_tickets), 0) FROM administrator_manages am " +
                "JOIN theater t ON am.theater_id = t.theater_id " +
                "JOIN screening s ON t.theater_id = s.theater_id " +
                "JOIN movies m ON s.movie_id = m.movie_id " +
                "JOIN ticket_sale ts ON s.screening_id = ts.screening_id " +
                "GROUP BY m.movie_id, t.theater_id " +
                "ORDER BY m.movie_id;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    MovieStat stat = new MovieStat();
                    stat.movie_name = rs.getString(1);
                    stat.theater_id = rs.getInt(2);
                    stat.revenue = rs.getDouble(3);
                    stat.seatsSold = rs.getInt(4);
                    results.add(stat);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting movie stats: " + e.getMessage());
        }
        return results;
    }

    public static class TheaterStat {
        int theater_id;
        double revenue;
        int ticketsSold;
        double avgOccupancy;
        int minOccupancy;
        int maxOccupancy;
    }

    public List<TheaterStat> theaterStats() {
        List<TheaterStat> results = new ArrayList<>();

        // total tickets sold per theater - stat.ticketsSold
        // total ticket sale revenue - stat.revenue
        // avg, min, max occupancy rate (% of occupied seats) for a theater - stat.avgOccupancy, stat.minOccupancy, stat.maxOccupancy

        String query = "SELECT t.theater_id, COALESCE(SUM(ts.total_price), 0.00), " +
                "COALESCE(SUM(ts.num_tickets), 0), AVG(ts.num_tickets::FLOAT), " +
                "MIN(ts.num_tickets), MAX(ts.num_tickets) " +
                "FROM administrator_manages am " +
                "JOIN theater t ON am.theater_id = t.theater_id " +
                "JOIN screening s ON t.theater_id = s.theater_id " +
                "JOIN movies m ON s.movie_id = m.movie_id " +
                "JOIN ticket_sale ts ON s.screening_id = ts.screening_id " +
                "GROUP BY t.theater_id " +
                "ORDER BY t.theater_id;";

        try (Connection conn = getConnection(); PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    TheaterStat stat = new TheaterStat();
                    stat.theater_id = rs.getInt(1);
                    stat.revenue = rs.getDouble(2);
                    stat.ticketsSold = rs.getInt(3);
                    stat.avgOccupancy = rs.getDouble(4);
                    stat.minOccupancy = rs.getInt(5);
                    stat.maxOccupancy = rs.getInt(6);
                    results.add(stat);
                }
            }
        } catch (Exception e) {
            System.out.println("Error getting theater stats: " + e.getMessage());
        }
        return results;
    }

    public Integer book(int screening_id, int num_tickets, double price, String card_num) {
        String query = "INSERT INTO ticket_sale (sale_id, user_id, screening_id, " +
                "num_tickets, purchase_date, total_price, card_number) VALUES " +
                "(?,?,?,?,CURRENT_TIMESTAMP,?,?);";
        String query1 = "SELECT COALESCE(MAX(sale_id), 0) + 1 FROM ticket_sale";
        String query2 = "UPDATE membership SET movies_watched = movies_watched + 1 WHERE membership_id = ?;";
        String query3 = "SELECT movies_watched FROM membership WHERE membership_id = ? FOR UPDATE;";
        String query4 = "UPDATE membership SET num_free_earned = num_free_earned + 1 WHERE membership_id = ?;";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                int sale_id;
                try (PreparedStatement stmt = conn.prepareStatement(query1)) {
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    sale_id = rs.getInt(1);
                }

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, sale_id);
                    stmt.setInt(2, user_id);
                    stmt.setInt(3, screening_id);
                    stmt.setInt(4, num_tickets);
                    stmt.setDouble(5, price);
                    stmt.setString(6, card_num);
                    stmt.executeUpdate();
                }

                try (PreparedStatement stmt = conn.prepareStatement(query2)) {
                    stmt.setInt(1, membership_id);
                    stmt.executeUpdate();
                }

                int num_watched;
                try (PreparedStatement stmt = conn.prepareStatement(query3)) {
                    stmt.setInt(1, membership_id);
                    ResultSet rs = stmt.executeQuery();
                    rs.next();
                    num_watched = rs.getInt("movies_watched");
                }

                if (num_watched % 10 == 0) {
                    try (PreparedStatement stmt = conn.prepareStatement(query4)) {
                        stmt.setInt(1, membership_id);
                        stmt.executeUpdate();
                    }
                }

                conn.commit();
                return sale_id;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                return null;
            }
        } catch (Exception e) {
            System.out.println("Connection Error: " + e.getMessage());
            return null;
        }
    }

    public int freeTickets(int num_tickets) {
        String query = "SELECT num_free_earned FROM membership WHERE membership_id = ? FOR UPDATE;";
        String query1 = "UPDATE membership SET num_free_earned = num_free_earned - ? WHERE membership_id = ?;";

        try (Connection conn = getConnection()) {
            conn.setAutoCommit(false);

            try {
                int curr;

                try (PreparedStatement stmt = conn.prepareStatement(query)) {
                    stmt.setInt(1, membership_id);
                    ResultSet rs = stmt.executeQuery();

                    if (!rs.next()) {
                        try {
                            conn.rollback();
                        } catch (Exception e) {
                            System.out.println("Rollback error: " + e);
                        }
                        return 0;
                    }

                    curr = rs.getInt("num_free_earned");
                }

                if (curr < num_tickets) {
                    try { conn.rollback(); } catch (Exception e) {
                        System.out.println("Rollback error: " + e);
                    }
                    return 0;
                }

                try (PreparedStatement stmt = conn.prepareStatement(query1)) {
                    stmt.setInt(1, num_tickets);
                    stmt.setInt(2, membership_id);
                    stmt.executeUpdate();
                }

                conn.commit();
                return num_tickets;
            } catch (Exception e) {
                try {
                    conn.rollback();
                } catch (Exception ex) {
                    System.out.println("Rollback error: " + ex);
                }
                return 0;
            }

        } catch (Exception e) {
            System.out.println("Connection Error: " + e);
            return 0;
        }
    }
}