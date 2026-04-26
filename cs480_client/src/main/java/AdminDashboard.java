import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminDashboard {

    private Stage stage;
    private GuiClient app;
    private Runnable onLogout;
    private Runnable onScreenings;
    private Runnable onAnalytics;
    private Runnable onMovies;
    private Runnable onPeople;
    private Runnable onTheaters;
    private Runnable onRegister;

    public AdminDashboard(Stage stage, GuiClient app, Runnable onLogout,
                          Runnable onScreenings, Runnable onAnalytics,
                          Runnable onMovies, Runnable onPeople,
                          Runnable onTheaters, Runnable onRegister) {
        this.stage       = stage;
        this.app         = app;
        this.onLogout    = onLogout;
        this.onScreenings = onScreenings;
        this.onAnalytics = onAnalytics;
        this.onMovies    = onMovies;
        this.onPeople    = onPeople;
        this.onTheaters  = onTheaters;
        this.onRegister = onRegister;
    }

    public Scene getScene() {

        Label wordmark = new Label("Velvet Screen · Admin");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button screeningsBtn = navButton("Screenings");
        Button analyticsBtn  = navButton("Analytics");
        Button moviesBtn     = navButton("Movies");
        Button peopleBtn     = navButton("People");
        Button theatersBtn   = navButton("Theaters");
        Button registerBtn     = navButton("Register");
        Button logoutBtn     = navButton("Logout");

        Button dashboardBtn = navButton("Dashboard");
        dashboardBtn.setStyle(dashboardBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        screeningsBtn.setOnAction(e -> { if (onScreenings != null) onScreenings.run(); });
        analyticsBtn.setOnAction(e ->  { if (onAnalytics != null) onAnalytics.run(); });
        moviesBtn.setOnAction(e ->     { if (onMovies != null) onMovies.run(); });
        peopleBtn.setOnAction(e ->     { if (onPeople != null) onPeople.run(); });
        theatersBtn.setOnAction(e ->   { if (onTheaters != null) onTheaters.run(); });
        registerBtn.setOnAction(e ->     { if (onRegister != null) onRegister.run(); });
        logoutBtn.setOnAction(e ->     { if (onLogout != null) onLogout.run(); });

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer,
            dashboardBtn, screeningsBtn, analyticsBtn, moviesBtn, peopleBtn, theatersBtn, registerBtn, logoutBtn);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        Label title = new Label("Dashboard");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        List<GuiClient.ScreeningStat> screeningStats = app.screeningStats();
        List<GuiClient.TheaterStat>   theaterStats   = app.theaterStats();
        List<GuiClient.MovieStat>     movieStats     = app.movieStats();

        double totalRevenue  = screeningStats.stream().mapToDouble(s -> s.revenue).sum();
        int    totalTickets  = screeningStats.stream().mapToInt(s -> s.seatsSold).sum();
        int    totalScreenings = screeningStats.size();

        double avgOccupancy = theaterStats.isEmpty() ? 0 :
            theaterStats.stream().mapToDouble(t -> t.avgOccupancy).average().orElse(0);

        HBox statsRow = new HBox(16,
            statCard("Total Revenue", String.format("$%.2f", totalRevenue)),
            statCard("Tickets Sold", String.valueOf(totalTickets)),
            statCard("Active Screenings", String.valueOf(totalScreenings)),
            statCard("Avg Occupancy", String.format("%.1f%%", avgOccupancy))
        );

        Label quickLinksTitle = new Label("QUICK LINKS");
        quickLinksTitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox quickLinks = new VBox(8,
            quickLink("→ View Screenings",  onScreenings),
            quickLink("→ Analytics",        onAnalytics),
            quickLink("→ Manage Movies",    onMovies),
            quickLink("→ Manage People",    onPeople),
            quickLink("→ Manage Theaters",  onTheaters)
        );

        Label movieTableTitle = new Label("REVENUE BY MOVIE");
        movieTableTitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox movieTable = new VBox(0);
        movieTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        HBox movieHeader = tableRow("MOVIE", "THEATER", "REVENUE", "TICKETS", true);
        movieTable.getChildren().add(movieHeader);

        for (GuiClient.MovieStat stat : movieStats) {
            HBox row = tableRow(
                stat.movie_name,
                "Theater " + stat.theater_id,
                String.format("$%.2f", stat.revenue),
                String.valueOf(stat.seatsSold),
                false
            );
            movieTable.getChildren().add(row);
        }

        if (movieStats.isEmpty()) {
            Label empty = new Label("No data available.");
            empty.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                "-fx-padding: 16;"
            );
            movieTable.getChildren().add(empty);
        }

        Label theaterTableTitle = new Label("REVENUE BY THEATER");
        theaterTableTitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox theaterTable = new VBox(0);
        theaterTable.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        HBox theaterHeader = tableRow("THEATER", "REVENUE", "TICKETS SOLD", "AVG OCCUPANCY", true);
        theaterTable.getChildren().add(theaterHeader);

        for (GuiClient.TheaterStat stat : theaterStats) {
            HBox row = tableRow(
                "Theater " + stat.theater_id,
                String.format("$%.2f", stat.revenue),
                String.valueOf(stat.ticketsSold),
                String.format("%.1f%%", stat.avgOccupancy),
                false
            );
            theaterTable.getChildren().add(row);
        }

        if (theaterStats.isEmpty()) {
            Label empty = new Label("No data available.");
            empty.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                "-fx-padding: 16;"
            );
            theaterTable.getChildren().add(empty);
        }

        VBox leftCol = new VBox(16, quickLinksTitle, quickLinks);
        leftCol.setPrefWidth(220);

        VBox rightCol = new VBox(24,
            statsRow,
            movieTableTitle, movieTable,
            theaterTableTitle, theaterTable
        );
        HBox.setHgrow(rightCol, Priority.ALWAYS);

        HBox body = new HBox(40, leftCol, rightCol);
        body.setPadding(new Insets(40));

        ScrollPane scroll = new ScrollPane(body);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setStyle(
            "-fx-background: " + LandingScreen.BG_COLOR + ";" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";" +
            "-fx-border-color: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );

        VBox root = new VBox(navbar, scroll);
        VBox.setVgrow(scroll, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        return new Scene(root, 1000, 750);
    }

    private VBox statCard(String label, String value) {
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label labelLabel = new Label(label);
        labelLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox card = new VBox(4, valueLabel, labelLabel);
        card.setPadding(new Insets(20));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private Label quickLink(String text, Runnable action) {
        Label link = new Label(text);
        link.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-cursor: hand;"
        );
        link.setOnMouseClicked(e -> { if (action != null) action.run(); });
        return link;
    }

    private HBox tableRow(String col1, String col2, String col3, String col4, boolean isHeader) {
        String style = isHeader ?
            "-fx-font-family: 'Questrial'; -fx-font-size: 11px; -fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" :
            "-fx-font-family: 'Questrial'; -fx-font-size: 13px; -fx-text-fill: " + LandingScreen.TEXT_DARK + ";";

        Label l1 = new Label(col1); l1.setStyle(style); l1.setPrefWidth(200);
        Label l2 = new Label(col2); l2.setStyle(style); l2.setPrefWidth(150);
        Label l3 = new Label(col3); l3.setStyle(style); l3.setPrefWidth(150);
        Label l4 = new Label(col4); l4.setStyle(style); HBox.setHgrow(l4, Priority.ALWAYS);

        HBox row = new HBox(l1, l2, l3, l4);
        row.setPadding(new Insets(12, 16, 12, 16));
        if (!isHeader) {
            row.setStyle("-fx-border-color: " + LandingScreen.BORDER_COLOR + " transparent transparent transparent;" +
                         "-fx-border-width: 1 0 0 0;");
        }
        return row;
    }

    private Button navButton(String text) {
        Button btn = new Button(text);
        btn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-cursor: hand;" +
            "-fx-padding: 6 12 6 12;"
        );
        return btn;
    }
}