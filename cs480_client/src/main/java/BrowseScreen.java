import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class BrowseScreen {

    private Stage stage;
    private GuiClient app;
    private String initialSearch;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onAccount;
    private Runnable onPayments;
    private java.util.function.Consumer<String[]> onViewDetails;

    public BrowseScreen(Stage stage, GuiClient app, String initialSearch,
                        Runnable onBack, Runnable onLogout,
                        Runnable onAccount, Runnable onPayments,
                        java.util.function.Consumer<String[]> onViewDetails) {
        this.stage         = stage;
        this.app           = app;
        this.initialSearch = initialSearch == null ? "" : initialSearch;
        this.onBack        = onBack;
        this.onLogout      = onLogout;
        this.onAccount     = onAccount;
        this.onPayments    = onPayments;
        this.onViewDetails = onViewDetails;
    }

    public Scene getScene() {

        // navbar
        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        wordmark.setOnMouseClicked(e -> { if (onBack != null) onBack.run(); });

        Button browseBtn   = navButton("Browse");
        Button accountBtn  = navButton("My Account");
        Button paymentsBtn = navButton("Payment Methods");
        Button logoutBtn   = navButton("Logout");

        browseBtn.setStyle(browseBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        accountBtn.setOnAction(e -> { if (onAccount != null) onAccount.run(); });
        paymentsBtn.setOnAction(e -> { if (onPayments != null) onPayments.run(); });
        logoutBtn.setOnAction(e -> { if (onLogout != null) onLogout.run(); });

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer, browseBtn, accountBtn, paymentsBtn, logoutBtn);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        // search bar
        TextField searchField = new TextField(initialSearch);
        searchField.setPromptText("Search by title...");
        searchField.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 12 10 12;" +
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-pref-width: 300px;"
        );

        // date picker
        DatePicker datePicker = new DatePicker();
        datePicker.setPrefWidth(110);

        // filters
        ComboBox<String> languageFilter = new ComboBox<>();
        languageFilter.getItems().addAll("All Languages", "English", "Spanish", "French", "Japanese", "Italian", "German");
        languageFilter.setStyle(comboStyle());

        ComboBox<String> genreFilter = new ComboBox<>();
        genreFilter.getItems().addAll("All Genres", "Horror", "Comedy", "Romance", "Action", "Crime", "Drama", "Fantasy", "Sci-Fi", "Thriller", "Musical", "Historical");
        genreFilter.setStyle(comboStyle());

        ComboBox<String> timeFilter = new ComboBox<>();
        timeFilter.getItems().addAll("Any Time", "Morning (before 12:00)", "Afternoon (12:00-17:00)", "Evening (after 17:00)");
        timeFilter.setStyle(comboStyle());

        Button searchBtn = new Button("Search");
        searchBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        HBox searchBar = new HBox(8, searchField, languageFilter, genreFilter, datePicker, timeFilter, searchBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // results area
        VBox resultsBox = new VBox(8);
        resultsBox.setFillWidth(true);

        // flag to prevent search during setup
        final boolean[] ready = {false};

        // search logic
        Runnable doSearch = () -> {
            if (!ready[0]) return;
            resultsBox.getChildren().clear();

            String search    = searchField.getText().trim();
            String lang      = languageFilter.getValue() == null ||
                               languageFilter.getValue().equals("All Languages") ? null : languageFilter.getValue();
            String genre     = genreFilter.getValue() == null ||
                               genreFilter.getValue().equals("All Genres") ? null : genreFilter.getValue();
            String date      = (datePicker.getValue() != null) ? datePicker.getValue().toString() : null;
            String timeStart = null;
            String timeEnd   = null;

            String t = timeFilter.getValue();
            if (t != null) {
                if (t.equals("Morning (before 12:00)")) {
                    timeStart = "00:00";
                    timeEnd   = "11:59";
                } else if (t.equals("Afternoon (12:00-17:00)")) {
                    timeStart = "12:00";
                    timeEnd   = "17:00";
                } else if (t.equals("Evening (after 17:00)")) {
                    timeStart = "17:01";
                    timeEnd   = "23:59";
                }
            }

            List<String[]> movies = app.getMovies(false, search, lang, genre, date, timeStart, timeEnd);

            if (movies.isEmpty()) {
                Label noResults = new Label("No screenings found. Try a different search.");
                noResults.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
                );
                resultsBox.getChildren().add(noResults);
            } else {
                for (String[] movie : movies) {
                    resultsBox.getChildren().add(movieCard(movie));
                }
            }
        };

        languageFilter.setValue("All Languages");
        genreFilter.setValue("All Genres");
        timeFilter.setValue("Any Time");

        searchBtn.setOnAction(e -> doSearch.run());
        searchField.setOnAction(e -> doSearch.run());
        languageFilter.setOnAction(e -> doSearch.run());
        genreFilter.setOnAction(e -> doSearch.run());
        timeFilter.setOnAction(e -> doSearch.run());
        datePicker.setOnAction(e -> doSearch.run());

        ready[0] = true;
        doSearch.run();

        ScrollPane scroll = new ScrollPane(resultsBox);
        scroll.setFitToWidth(true);
        scroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scroll.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scroll.setStyle(
            "-fx-background: " + LandingScreen.BG_COLOR + ";" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";" +
            "-fx-border-color: transparent;" +
            "-fx-focus-color: transparent;" +
            "-fx-faint-focus-color: transparent;"
        );

        VBox content = new VBox(16, searchBar, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox root = new VBox(navbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        return new Scene(root, 1000, 750);
    }

    private HBox movieCard(String[] movie) {
        String title       = movie[1];
        String language    = movie[2];
        String releaseDate = movie[3] != null ? movie[3].substring(0, 4) : "";
        String is3d        = "true".equals(movie[9]) ? "3D" : "";
        String sound       = "true".equals(movie[10]) ? "Sound" : "";

        String features = "";
        if (!is3d.isEmpty() && !sound.isEmpty()) features = "3D · Sound";
        else if (!is3d.isEmpty()) features = "3D";
        else if (!sound.isEmpty()) features = "Sound";

        Label titleLabel = new Label(title);
        titleLabel.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 16px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label metaLabel = new Label(releaseDate + " · " + language +
            (features.isEmpty() ? "" : " · " + features));
        metaLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox info = new VBox(4, titleLabel, metaLabel);
        info.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(info, Priority.ALWAYS);

        Button viewBtn = new Button("View Details");
        viewBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 8 16 8 16;" +
            "-fx-border-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        viewBtn.setOnAction(e -> {
            if (onViewDetails != null) onViewDetails.accept(movie);
        });

        HBox card = new HBox(16, info, viewBtn);
        card.setAlignment(Pos.CENTER_LEFT);
        card.setPadding(new Insets(16));
        card.setStyle(
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        return card;
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

    private String comboStyle() {
        return "-fx-font-family: 'Questrial';" +
               "-fx-font-size: 12px;" +
               "-fx-background-color: " + LandingScreen.WHITE + ";" +
               "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
               "-fx-border-radius: 4;" +
               "-fx-background-radius: 4;";
    }
}