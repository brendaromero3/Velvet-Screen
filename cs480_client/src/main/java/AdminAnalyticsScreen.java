import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminAnalyticsScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onScreenings;
    private Runnable onMovies;
    private Runnable onPeople;
    private Runnable onTheaters;

    public AdminAnalyticsScreen(Stage stage, GuiClient app, Runnable onBack,
                                 Runnable onLogout, Runnable onScreenings,
                                 Runnable onMovies, Runnable onPeople, Runnable onTheaters) {
        this.stage       = stage;
        this.app         = app;
        this.onBack      = onBack;
        this.onLogout    = onLogout;
        this.onScreenings = onScreenings;
        this.onMovies    = onMovies;
        this.onPeople    = onPeople;
        this.onTheaters  = onTheaters;
    }

    public Scene getScene() {

        Label wordmark = new Label("Velvet Screen · Admin");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        wordmark.setOnMouseClicked(e -> { if (onBack != null) onBack.run(); });

        Button dashboardBtn  = navButton("Dashboard");
        Button screeningsBtn = navButton("Screenings");
        Button analyticsBtn  = navButton("Analytics");
        Button moviesBtn     = navButton("Movies");
        Button peopleBtn     = navButton("People");
        Button theatersBtn   = navButton("Theaters");
        Button logoutBtn     = navButton("Logout");

        analyticsBtn.setStyle(analyticsBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        dashboardBtn.setOnAction(e ->  { if (onBack != null) onBack.run(); });
        screeningsBtn.setOnAction(e -> { if (onScreenings != null) onScreenings.run(); });
        moviesBtn.setOnAction(e ->     { if (onMovies != null) onMovies.run(); });
        peopleBtn.setOnAction(e ->     { if (onPeople != null) onPeople.run(); });
        theatersBtn.setOnAction(e ->   { if (onTheaters != null) onTheaters.run(); });
        logoutBtn.setOnAction(e ->     { if (onLogout != null) onLogout.run(); });

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer,
            dashboardBtn, screeningsBtn, analyticsBtn, moviesBtn, peopleBtn, theatersBtn, logoutBtn);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        Label title = new Label("Analytics");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        List<GuiClient.ScreeningStat> screeningStats = app.screeningStats();
        List<GuiClient.TheaterStat>   theaterStats   = app.theaterStats();
        List<GuiClient.MovieStat>     movieStats     = app.movieStats();

        double totalRevenue = screeningStats.stream().mapToDouble(s -> s.revenue).sum();
        int totalTickets    = screeningStats.stream().mapToInt(s -> s.seatsSold).sum();
        double avgOcc = theaterStats.isEmpty() ? 0 :
            theaterStats.stream().mapToDouble(t -> t.avgOccupancy).average().orElse(0);
        int minOcc = theaterStats.isEmpty() ? 0 :
            theaterStats.stream().mapToInt(t -> t.minOccupancy).min().orElse(0);
        int maxOcc = theaterStats.isEmpty() ? 0 :
            theaterStats.stream().mapToInt(t -> t.maxOccupancy).max().orElse(0);

        HBox summaryRow = new HBox(16,
            statCard("Total Revenue",   String.format("$%.2f", totalRevenue)),
            statCard("Total Tickets",   String.valueOf(totalTickets)),
            statCard("Avg Occupancy",   String.format("%.1f%%", avgOcc)),
            statCard("Min Occupancy",   String.valueOf(minOcc) + " tickets"),
            statCard("Max Occupancy",   String.valueOf(maxOcc) + " tickets")
        );

        Label movieTableTitle = new Label("REVENUE BY MOVIE");
        movieTableTitle.setStyle(sectionLabelStyle());

        VBox movieTable = buildTable(
            new String[]{"MOVIE", "THEATER", "REVENUE", "TICKETS SOLD"},
            new int[]{250, 120, 150, 150}
        );
        for (GuiClient.MovieStat stat : movieStats) {
            addTableRow(movieTable,
                new String[]{
                    stat.movie_name,
                    "Theater " + stat.theater_id,
                    String.format("$%.2f", stat.revenue),
                    String.valueOf(stat.seatsSold)
                },
                new int[]{250, 120, 150, 150}
            );
        }
        if (movieStats.isEmpty()) addEmptyRow(movieTable);

        Label theaterTableTitle = new Label("REVENUE & OCCUPANCY BY THEATER");
        theaterTableTitle.setStyle(sectionLabelStyle());

        VBox theaterTable = buildTable(
            new String[]{"THEATER", "REVENUE", "TICKETS SOLD", "AVG OCC", "MIN OCC", "MAX OCC"},
            new int[]{150, 150, 130, 100, 100, 100}
        );
        for (GuiClient.TheaterStat stat : theaterStats) {
            addTableRow(theaterTable,
                new String[]{
                    "Theater " + stat.theater_id,
                    String.format("$%.2f", stat.revenue),
                    String.valueOf(stat.ticketsSold),
                    String.format("%.1f%%", stat.avgOccupancy),
                    String.valueOf(stat.minOccupancy),
                    String.valueOf(stat.maxOccupancy)
                },
                new int[]{150, 150, 130, 100, 100, 100}
            );
        }
        if (theaterStats.isEmpty()) addEmptyRow(theaterTable);

        Label screeningTableTitle = new Label("REVENUE BY SCREENING");
        screeningTableTitle.setStyle(sectionLabelStyle());

        VBox screeningTable = buildTable(
            new String[]{"MOVIE", "THEATER", "SCREENING ID", "REVENUE", "TICKETS", "OCC %"},
            new int[]{200, 100, 120, 130, 100, 100}
        );
        for (GuiClient.ScreeningStat stat : screeningStats) {
            double occPct = stat.capacity > 0 ?
                (stat.seatsSold * 100.0 / stat.capacity) : 0;
            addTableRow(screeningTable,
                new String[]{
                    stat.movie_name,
                    "Theater " + stat.theater_id,
                    "#" + stat.screening_id,
                    String.format("$%.2f", stat.revenue),
                    String.valueOf(stat.seatsSold),
                    String.format("%.0f%%", occPct)
                },
                new int[]{200, 100, 120, 130, 100, 100}
            );
        }
        if (screeningStats.isEmpty()) addEmptyRow(screeningTable);

        VBox content = new VBox(24,
            title,
            summaryRow,
            movieTableTitle, movieTable,
            theaterTableTitle, theaterTable,
            screeningTableTitle, screeningTable
        );
        content.setPadding(new Insets(40));

        ScrollPane scroll = new ScrollPane(content);
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

    private VBox buildTable(String[] headers, int[] widths) {
        VBox table = new VBox(0);
        table.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        HBox headerRow = new HBox();
        headerRow.setPadding(new Insets(12, 16, 12, 16));
        for (int i = 0; i < headers.length; i++) {
            Label h = new Label(headers[i]);
            h.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 11px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
            );
            if (i == headers.length - 1) {
                HBox.setHgrow(h, Priority.ALWAYS);
            } else {
                h.setPrefWidth(widths[i]);
            }
            headerRow.getChildren().add(h);
        }
        table.getChildren().add(headerRow);
        return table;
    }

    private void addTableRow(VBox table, String[] values, int[] widths) {
        HBox row = new HBox();
        row.setPadding(new Insets(12, 16, 12, 16));
        row.setStyle(
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + " transparent transparent transparent;" +
            "-fx-border-width: 1 0 0 0;"
        );
        for (int i = 0; i < values.length; i++) {
            Label l = new Label(values[i]);
            l.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
            );
            if (i == values.length - 1) {
                HBox.setHgrow(l, Priority.ALWAYS);
            } else {
                l.setPrefWidth(widths[i]);
            }
            row.getChildren().add(l);
        }
        table.getChildren().add(row);
    }

    private void addEmptyRow(VBox table) {
        Label empty = new Label("No data available.");
        empty.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
            "-fx-padding: 16;"
        );
        table.getChildren().add(empty);
    }

    private VBox statCard(String label, String value) {
        Label valueLabel = new Label(value);
        valueLabel.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 22px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );
        Label labelLabel = new Label(label);
        labelLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );
        VBox card = new VBox(4, valueLabel, labelLabel);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );
        HBox.setHgrow(card, Priority.ALWAYS);
        return card;
    }

    private String sectionLabelStyle() {
        return "-fx-font-family: 'Questrial';" +
               "-fx-font-size: 11px;" +
               "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";
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