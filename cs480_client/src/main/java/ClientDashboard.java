import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class ClientDashboard {

    private Stage stage;
    private GuiClient app;
    private Runnable onLogout;
    private Runnable onAccount;
    private Runnable onPayments;
    private java.util.function.Consumer<String> onBrowse;

    public ClientDashboard(Stage stage, GuiClient app, Runnable onLogout, java.util.function.Consumer<String> onBrowse, Runnable onAccount, Runnable onPayments) {
        this.stage      = stage;
        this.app        = app;
        this.onLogout   = onLogout;
        this.onBrowse   = onBrowse;
        this.onAccount  = onAccount;
        this.onPayments = onPayments;
    }

    public Scene getScene() {
        String clientName   = app.getClientName();
        int moviesWatched   = app.getMoviesWatched();

        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button browseBtn   = navButton("Browse");
        Button accountBtn  = navButton("My Account");
        Button paymentsBtn = navButton("Payment Methods");
        Button logoutBtn   = navButton("Logout");

        browseBtn.setOnAction(e -> { if (onBrowse != null) onBrowse.accept(""); });
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

        Label welcome = new Label("Welcome back, " + clientName + ".");
        welcome.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label whatShowing = new Label("What would you like to watch today?");
        whatShowing.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        TextField searchField = new TextField();
        searchField.setPromptText("Search by title...");
        searchField.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 12 10 12;" +
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-pref-width: 400px;"
        );

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
        searchBtn.setOnAction(e -> {
            if (onBrowse != null) onBrowse.accept(searchField.getText().trim());
        });
        searchField.setOnAction(e -> {
            if (onBrowse != null) onBrowse.accept(searchField.getText().trim());
        });

        HBox searchBar = new HBox(8, searchField, searchBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        Label rewardTitle = new Label("REWARD PROGRAM");
        rewardTitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        Label rewardCount = new Label(moviesWatched + " / 10");
        rewardCount.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        Label rewardSub = new Label("tickets to your next free movie.");
        rewardSub.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        HBox rewardInfo = new HBox(6, rewardCount, rewardSub);
        rewardInfo.setAlignment(Pos.CENTER_LEFT);

        double progress = (moviesWatched % 10) / 10.0;
        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefWidth(400);
        progressBar.setStyle("-fx-accent: " + LandingScreen.SUCCESS_COLOR + ";");

        VBox rewardBox = new VBox(6, rewardTitle, rewardInfo, progressBar);

        Label nowShowingTitle = new Label("NOW SHOWING");
        nowShowingTitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        VBox nowShowingBox = new VBox(8);
        List<String[]> movies = app.getMovies(false, null, null, null, null, null, null);

        if (movies.isEmpty()) {
            Label noMovies = new Label("No movies currently showing.");
            noMovies.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
            );
            nowShowingBox.getChildren().add(noMovies);
        } else {
            int limit = Math.min(3, movies.size());
            for (int i = 0; i < limit; i++) {
                String[] movie = movies.get(i);
                String movieTitle = movie[1];
                String language   = movie[2];
                String year       = movie[3] != null && movie[3].length() >= 4 ? movie[3].substring(0, 4) : "";

                Label movieLabel = new Label(movieTitle);
                movieLabel.setStyle(
                    "-fx-font-family: 'Noto Serif Display';" +
                    "-fx-font-size: 15px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
                );

                Label metaLabel = new Label(year + " · " + language);
                metaLabel.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 12px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
                );

                VBox info = new VBox(4, movieLabel, metaLabel);
                HBox.setHgrow(info, Priority.ALWAYS);

                Button viewBtn = new Button("View Details");
                viewBtn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 12px;" +
                    "-fx-padding: 6 14 6 14;" +
                    "-fx-border-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-cursor: hand;"
                );
                final String[] finalMovie = movie;
                viewBtn.setOnAction(e -> {
                    if (onBrowse != null) onBrowse.accept(finalMovie[1]);
                });

                HBox card = new HBox(16, info, viewBtn);
                card.setAlignment(Pos.CENTER_LEFT);
                card.setPadding(new Insets(14));
                card.setStyle(
                    "-fx-background-color: " + LandingScreen.WHITE + ";" +
                    "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
                    "-fx-border-radius: 8;" +
                    "-fx-background-radius: 8;"
                );
                nowShowingBox.getChildren().add(card);
            }

            Button seeAllBtn = new Button("See all movies →");
            seeAllBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;"
            );
            seeAllBtn.setOnAction(e -> { if (onBrowse != null) onBrowse.accept(""); });
            nowShowingBox.getChildren().add(seeAllBtn);
        }

        VBox content = new VBox(20,
            welcome,
            whatShowing,
            searchBar,
            new Separator(),
            rewardBox,
            new Separator(),
            nowShowingTitle,
            nowShowingBox
        );
        content.setPadding(new Insets(40));
        content.setMaxWidth(700);

        HBox contentWrapper = new HBox(content);
        contentWrapper.setAlignment(Pos.TOP_CENTER);

        ScrollPane scroll = new ScrollPane(contentWrapper);
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