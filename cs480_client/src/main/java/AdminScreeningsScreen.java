import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminScreeningsScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onAnalytics;
    private Runnable onMovies;
    private Runnable onPeople;
    private Runnable onTheaters;
    private Runnable onRegister;

    public AdminScreeningsScreen(Stage stage, GuiClient app, Runnable onBack,
                                  Runnable onLogout, Runnable onAnalytics,
                                  Runnable onMovies, Runnable onPeople,
                                  Runnable onTheaters, Runnable onRegister) {
        this.stage      = stage;
        this.app        = app;
        this.onBack     = onBack;
        this.onLogout   = onLogout;
        this.onAnalytics = onAnalytics;
        this.onMovies   = onMovies;
        this.onPeople   = onPeople;
        this.onTheaters = onTheaters;
        this.onRegister = onRegister;
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
        Button registerBtn     = navButton("Register");
        Button logoutBtn     = navButton("Logout");

        screeningsBtn.setStyle(screeningsBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        dashboardBtn.setOnAction(e -> { if (onBack != null) onBack.run(); });
        analyticsBtn.setOnAction(e -> { if (onAnalytics != null) onAnalytics.run(); });
        moviesBtn.setOnAction(e ->    { if (onMovies != null) onMovies.run(); });
        peopleBtn.setOnAction(e ->    { if (onPeople != null) onPeople.run(); });
        theatersBtn.setOnAction(e ->  { if (onTheaters != null) onTheaters.run(); });
        registerBtn.setOnAction(e ->     { if (onRegister != null) onRegister.run(); });
        logoutBtn.setOnAction(e ->    { if (onLogout != null) onLogout.run(); });

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

        Label title = new Label("Screenings");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button scheduleBtn = new Button("+ Schedule New Screening");
        scheduleBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);
        HBox titleRow = new HBox(title, titleSpacer, scheduleBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox scheduleForm = new VBox(10);
        scheduleForm.setVisible(false);
        scheduleForm.setManaged(false);
        scheduleForm.setPadding(new Insets(20));
        scheduleForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label formTitle = new Label("Schedule New Screening");
        formTitle.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label movieLabel = new Label("MOVIE TITLE");
        movieLabel.setStyle(fieldLabelStyle());
        TextField movieField = new TextField();
        movieField.setPromptText("Enter exact movie title");
        movieField.setStyle(fieldStyle());

        Label theaterLabel = new Label("THEATER ID");
        theaterLabel.setStyle(fieldLabelStyle());
        TextField theaterField = new TextField();
        theaterField.setPromptText("e.g. 1, 2, 3");
        theaterField.setStyle(fieldStyle());

        Label dateLabel = new Label("DATE");
        dateLabel.setStyle(fieldLabelStyle());
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(340);

        Label timeLabel = new Label("TIME (BETWEEN 08:00 AND 00:00)");
        timeLabel.setStyle(fieldLabelStyle());
        TextField timeField = new TextField();
        timeField.setPromptText("e.g. 19:30");
        timeField.setStyle(fieldStyle());

        Label formError = new Label("");
        formError.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Label formSuccess = new Label("");
        formSuccess.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        Button saveBtn = new Button("Save Screening");
        saveBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        Button cancelFormBtn = new Button("Cancel");
        cancelFormBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        cancelFormBtn.setOnAction(e -> {
            scheduleForm.setVisible(false);
            scheduleForm.setManaged(false);
            formError.setText("");
            formSuccess.setText("");
            movieField.clear();
            theaterField.clear();
            timeField.clear();
            datePicker.setValue(null);
        });

        HBox formButtons = new HBox(8, saveBtn, cancelFormBtn);

        scheduleForm.getChildren().addAll(
            formTitle,
            movieLabel, movieField,
            theaterLabel, theaterField,
            dateLabel, datePicker,
            timeLabel, timeField,
            formError, formSuccess,
            formButtons
        );

        scheduleBtn.setOnAction(e -> {
            scheduleForm.setVisible(true);
            scheduleForm.setManaged(true);
        });

        VBox tableBox = new VBox(0);
        tableBox.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        String headerStyle =
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";

        Label hMovie    = new Label("MOVIE");       hMovie.setStyle(headerStyle);    hMovie.setPrefWidth(200);
        Label hTheater  = new Label("THEATER");     hTheater.setStyle(headerStyle);  hTheater.setPrefWidth(100);
        Label hDateTime = new Label("DATE & TIME"); hDateTime.setStyle(headerStyle); hDateTime.setPrefWidth(180);
        Label hSold     = new Label("SOLD");        hSold.setStyle(headerStyle);     hSold.setPrefWidth(80);
        Label hCapacity = new Label("CAPACITY");    hCapacity.setStyle(headerStyle); hCapacity.setPrefWidth(80);
        Label hOcc      = new Label("OCC %");       hOcc.setStyle(headerStyle);      hOcc.setPrefWidth(80);
        Label hRevenue  = new Label("REVENUE");     hRevenue.setStyle(headerStyle);  HBox.setHgrow(hRevenue, Priority.ALWAYS);

        HBox tableHeader = new HBox(hMovie, hTheater, hDateTime, hSold, hCapacity, hOcc, hRevenue);
        tableHeader.setPadding(new Insets(12, 16, 12, 16));
        tableBox.getChildren().add(tableHeader);

        List<GuiClient.ScreeningStat> stats = app.screeningStats();

        if (stats.isEmpty()) {
            Label empty = new Label("No screenings found.");
            empty.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                "-fx-padding: 16;"
            );
            tableBox.getChildren().add(empty);
        } else {
            for (GuiClient.ScreeningStat stat : stats) {
                double occPct = stat.capacity > 0 ?
                    (stat.seatsSold * 100.0 / stat.capacity) : 0;

                String rowStyle =
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";";

                Label rMovie    = new Label(stat.movie_name);
                Label rTheater  = new Label("Theater " + stat.theater_id);
                Label rDateTime = new Label("Screening #" + stat.screening_id);
                Label rSold     = new Label(String.valueOf(stat.seatsSold));
                Label rCapacity = new Label(String.valueOf(stat.capacity));
                Label rOcc      = new Label(String.format("%.0f%%", occPct));
                Label rRevenue  = new Label(String.format("$%.2f", stat.revenue));

                rMovie.setStyle(rowStyle);    rMovie.setPrefWidth(200);
                rTheater.setStyle(rowStyle);  rTheater.setPrefWidth(100);
                rDateTime.setStyle(rowStyle); rDateTime.setPrefWidth(180);
                rSold.setStyle(rowStyle);     rSold.setPrefWidth(80);
                rCapacity.setStyle(rowStyle); rCapacity.setPrefWidth(80);
                rOcc.setStyle(rowStyle);      rOcc.setPrefWidth(80);
                rRevenue.setStyle(rowStyle);  HBox.setHgrow(rRevenue, Priority.ALWAYS);

                HBox row = new HBox(rMovie, rTheater, rDateTime, rSold, rCapacity, rOcc, rRevenue);
                row.setPadding(new Insets(12, 16, 12, 16));
                row.setStyle(
                    "-fx-border-color: " + LandingScreen.BORDER_COLOR + " transparent transparent transparent;" +
                    "-fx-border-width: 1 0 0 0;"
                );
                tableBox.getChildren().add(row);
            }
        }

        saveBtn.setOnAction(e -> {
            formError.setText("");
            formSuccess.setText("");

            String movieName = movieField.getText().trim();
            String theaterIdStr = theaterField.getText().trim();
            String time = timeField.getText().trim();

            if (movieName.isEmpty() || theaterIdStr.isEmpty() ||
                datePicker.getValue() == null || time.isEmpty()) {
                formError.setText("Please fill in all fields.");
                return;
            }

            String[] parts = time.split(":");
            if (parts.length != 2) {
                formError.setText("Invalid time format. Use HH:MM");
                return;
            }

            int hour, min;
            hour = Integer.parseInt(parts[0]);
            min = Integer.parseInt(parts[1]);

            if (hour < 8 || (hour == 20 &&  min > 0) || hour > 23) {
                formError.setText("Screening time must be between 08:00 and 00:00");
                return;
            }

            int theaterId;
            try {
                theaterId = Integer.parseInt(theaterIdStr);
            } catch (NumberFormatException ex) {
                formError.setText("Theater ID must be a number.");
                return;
            }

            String date = datePicker.getValue().toString();

            boolean success = app.schedule(movieName, theaterId, date + " ", time + ":00");

            if (success) {
                formSuccess.setText("Screening scheduled successfully!");
                movieField.clear();
                theaterField.clear();
                timeField.clear();
                datePicker.setValue(null);
            } else {
                formError.setText("Failed to schedule. Check movie title and theater ID.");
            }
        });

        VBox content = new VBox(24, titleRow, scheduleForm, tableBox);
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

    private String fieldLabelStyle() {
        return "-fx-font-family: 'Questrial';" +
               "-fx-font-size: 11px;" +
               "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";
    }

    private String fieldStyle() {
        return "-fx-font-family: 'Questrial';" +
               "-fx-font-size: 13px;" +
               "-fx-padding: 10 12 10 12;" +
               "-fx-background-color: " + LandingScreen.WHITE + ";" +
               "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
               "-fx-border-radius: 4;" +
               "-fx-background-radius: 4;" +
               "-fx-pref-width: 340px;";
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