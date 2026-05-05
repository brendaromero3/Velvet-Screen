import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminMoviesScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onScreenings;
    private Runnable onAnalytics;
    private Runnable onPeople;
    private Runnable onTheaters;
    private Runnable onRegister;

    public AdminMoviesScreen(Stage stage, GuiClient app, Runnable onBack,
                              Runnable onLogout, Runnable onScreenings,
                              Runnable onAnalytics, Runnable onPeople,
                              Runnable onTheaters, Runnable onRegister) {
        this.stage       = stage;
        this.app         = app;
        this.onBack      = onBack;
        this.onLogout    = onLogout;
        this.onScreenings = onScreenings;
        this.onAnalytics = onAnalytics;
        this.onPeople    = onPeople;
        this.onTheaters  = onTheaters;
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

        moviesBtn.setStyle(moviesBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        dashboardBtn.setOnAction(e ->  { if (onBack != null) onBack.run(); });
        screeningsBtn.setOnAction(e -> { if (onScreenings != null) onScreenings.run(); });
        analyticsBtn.setOnAction(e ->  { if (onAnalytics != null) onAnalytics.run(); });
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

        Label title = new Label("Movies");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button addMovieBtn = new Button("+ Add New Movie");
        addMovieBtn.setStyle(
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
        HBox titleRow = new HBox(title, titleSpacer, addMovieBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox movieForm = new VBox(10);
        movieForm.setVisible(false);
        movieForm.setManaged(false);
        movieForm.setPadding(new Insets(20));
        movieForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label formTitle = new Label("Add New Movie");
        formTitle.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label titleLabel = new Label("TITLE");
        titleLabel.setStyle(fieldLabelStyle());
        TextField titleField = new TextField();
        titleField.setPromptText("Movie title");
        titleField.setStyle(fieldStyle());

        Label langLabel = new Label("ORIGINAL LANGUAGE");
        langLabel.setStyle(fieldLabelStyle());
        TextField langField = new TextField();
        langField.setPromptText("e.g. English, Spanish");
        langField.setStyle(fieldStyle());

        Label releaseDateLabel = new Label("RELEASE DATE");
        releaseDateLabel.setStyle(fieldLabelStyle());
        DatePicker releaseDatePicker = new DatePicker();
        releaseDatePicker.setPrefWidth(340);

        Label lengthLabel = new Label("LENGTH (minutes)");
        lengthLabel.setStyle(fieldLabelStyle());
        TextField lengthField = new TextField();
        lengthField.setPromptText("e.g. 120");
        lengthField.setStyle(fieldStyle());

        Label majorStudioLabel = new Label("MAJOR STUDIO");
        majorStudioLabel.setStyle(fieldLabelStyle());
        ToggleGroup studioGroup = new ToggleGroup();
        ToggleButton yesBtn = new ToggleButton("Yes");
        ToggleButton noBtn  = new ToggleButton("No");
        yesBtn.setToggleGroup(studioGroup);
        noBtn.setToggleGroup(studioGroup);
        noBtn.setSelected(true);

        String toggleSelected =
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 20 6 20;" +
            "-fx-cursor: hand;";
        String toggleUnselected =
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 20 6 20;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-cursor: hand;";

        yesBtn.setStyle(toggleUnselected);
        noBtn.setStyle(toggleSelected);

        yesBtn.setOnAction(e -> { yesBtn.setStyle(toggleSelected); noBtn.setStyle(toggleUnselected); });
        noBtn.setOnAction(e ->  { noBtn.setStyle(toggleSelected);  yesBtn.setStyle(toggleUnselected); });

        HBox studioToggle = new HBox(0, yesBtn, noBtn);

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

        Button saveMovieBtn = new Button("Save Movie");
        saveMovieBtn.setStyle(
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
            movieForm.setVisible(false);
            movieForm.setManaged(false);
            formError.setText("");
            formSuccess.setText("");
            titleField.clear();
            langField.clear();
            lengthField.clear();
            releaseDatePicker.setValue(null);
            noBtn.setSelected(true);
            noBtn.setStyle(toggleSelected);
            yesBtn.setStyle(toggleUnselected);
        });

        HBox formButtons = new HBox(8, saveMovieBtn, cancelFormBtn);

        movieForm.getChildren().addAll(
            formTitle,
            titleLabel, titleField,
            langLabel, langField,
            releaseDateLabel, releaseDatePicker,
            lengthLabel, lengthField,
            majorStudioLabel, studioToggle,
            formError, formSuccess,
            formButtons
        );

        addMovieBtn.setOnAction(e -> {
            formTitle.setText("Add New Movie");
            movieForm.setVisible(true);
            movieForm.setManaged(true);
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

        Label hTitle    = new Label("TITLE");     hTitle.setStyle(headerStyle);    hTitle.setPrefWidth(220);
        Label hLang     = new Label("LANGUAGE");  hLang.setStyle(headerStyle);     hLang.setPrefWidth(120);
        Label hYear     = new Label("YEAR");       hYear.setStyle(headerStyle);     hYear.setPrefWidth(80);
        Label hLength   = new Label("LENGTH");    hLength.setStyle(headerStyle);   hLength.setPrefWidth(80);
        Label hMajor    = new Label("MAJOR");     hMajor.setStyle(headerStyle);    hMajor.setPrefWidth(80);
        Label hDirector = new Label("DIRECTOR");  hDirector.setStyle(headerStyle); hDirector.setPrefWidth(180);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        Label hActions = new Label("ACTIONS"); hActions.setStyle(headerStyle);

        HBox tableHeader = new HBox(hTitle, hLang, hYear, hLength, hMajor, hDirector, headerSpacer, hActions);
        tableHeader.setPadding(new Insets(12, 16, 12, 16));
        tableBox.getChildren().add(tableHeader);

        final Runnable[] reloadRef = new Runnable[1];

        reloadRef[0] = () -> {
            tableBox.getChildren().clear();
            tableBox.getChildren().add(tableHeader);

            List<String[]> movies = app.getAllMovies();

            if (movies.isEmpty()) {
                Label empty = new Label("No movies found.");
                empty.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                    "-fx-padding: 16;"
                );
                tableBox.getChildren().add(empty);
                return;
            }

            for (String[] movie : movies) {
                // movie[0]=id, [1]=title, [2]=language, [3]=release_date, [4]=length, [5]=major_studio, [6]=directors
                String year     = movie[3] != null && movie[3].length() >= 4 ? movie[3].substring(0, 4) : "";
                int    lengthMin = movie[4] != null ? Integer.parseInt(movie[4]) : 0;
                String lengthStr = (lengthMin / 60) + "h " + (lengthMin % 60) + "m";
                String major    = "t".equals(movie[5]) ? "Yes" : "No";
                String director = movie[6] != null ? movie[6] : "—";

                String rowStyle =
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";";

                Label rTitle    = new Label(movie[1]); rTitle.setStyle(rowStyle);    rTitle.setPrefWidth(220);
                Label rLang     = new Label(movie[2]); rLang.setStyle(rowStyle);     rLang.setPrefWidth(120);
                Label rYear     = new Label(year);      rYear.setStyle(rowStyle);     rYear.setPrefWidth(80);
                Label rLength   = new Label(lengthStr); rLength.setStyle(rowStyle);   rLength.setPrefWidth(80);
                Label rMajor    = new Label(major);     rMajor.setStyle(rowStyle);    rMajor.setPrefWidth(80);
                Label rDirector = new Label(director);  rDirector.setStyle(rowStyle); rDirector.setPrefWidth(180);

                Button deleteBtn = new Button("Delete");
                deleteBtn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";" +
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 12px;" +
                    "-fx-padding: 4 10 4 10;" +
                    "-fx-border-color: " + LandingScreen.ERROR_COLOR + ";" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-cursor: hand;"
                );

                int movieId = Integer.parseInt(movie[0]);
                deleteBtn.setOnAction(e -> {
                    boolean deleted = app.deleteMovie(movieId);
                    if (deleted) reloadRef[0].run();
                });

                Region rowSpacer = new Region();
                HBox.setHgrow(rowSpacer, Priority.ALWAYS);
                HBox actions = new HBox(rowSpacer, deleteBtn);
                HBox.setHgrow(actions, Priority.ALWAYS);

                HBox row = new HBox(rTitle, rLang, rYear, rLength, rMajor, rDirector, actions);
                row.setPadding(new Insets(12, 16, 12, 16));
                row.setAlignment(Pos.CENTER_LEFT);
                row.setStyle(
                    "-fx-border-color: " + LandingScreen.BORDER_COLOR + " transparent transparent transparent;" +
                    "-fx-border-width: 1 0 0 0;"
                );
                tableBox.getChildren().add(row);
            }
        };

        reloadRef[0].run();

        saveMovieBtn.setOnAction(e -> {
            formError.setText("");
            formSuccess.setText("");

            String t  = titleField.getText().trim();
            String l  = langField.getText().trim();
            String len = lengthField.getText().trim();

            if (t.isEmpty() || l.isEmpty() || releaseDatePicker.getValue() == null || len.isEmpty()) {
                formError.setText("Please fill in all fields.");
                return;
            }

            int lengthVal;
            try {
                lengthVal = Integer.parseInt(len);
            } catch (NumberFormatException ex) {
                formError.setText("Length must be a number in minutes.");
                return;
            }

            String releaseDate = releaseDatePicker.getValue().toString();
            boolean isMajor    = yesBtn.isSelected();

            boolean success = app.addMovie(t, l, releaseDate, lengthVal, isMajor);
            if (success) {
                formSuccess.setText("Movie added successfully!");
                titleField.clear();
                langField.clear();
                lengthField.clear();
                releaseDatePicker.setValue(null);
                reloadRef[0].run();
            } else {
                formError.setText("Failed to add movie.");
            }
        });

        VBox content = new VBox(24, titleRow, movieForm, tableBox);
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