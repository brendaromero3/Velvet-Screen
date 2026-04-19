import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

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
        String clientName    = app.getClientName();
        int moviesWatched    = app.getMoviesWatched();
        int moviesUntilFree  = 10 - (moviesWatched % 10);

        // navbar
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

        browseBtn.setOnAction(e -> {
            if (onBrowse != null) onBrowse.accept("");
        });        
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

        // welcome
        Label welcome = new Label("Welcome back, " + clientName + ".");
        welcome.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label whatShowing = new Label("What's showing?");
        whatShowing.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 14px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        // search bar
        TextField searchField = new TextField();
        searchField.setPromptText("Search by title, director, or actor...");
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

        HBox searchBar = new HBox(8, searchField, searchBtn);
        searchBar.setAlignment(Pos.CENTER_LEFT);

        // quick filters
        Label filtersLabel = new Label("QUICK FILTERS");
        filtersLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        HBox filters = new HBox(8,
            filterChip("Today"),
            filterChip("This Weekend"),
            filterChip("3D"),
            filterChip("Original Language")
        );

        // reward progress
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

        // progress bar
        double progress = (moviesWatched % 10) / 10.0;
        ProgressBar progressBar = new ProgressBar(progress);
        progressBar.setPrefWidth(400);
        progressBar.setStyle(
            "-fx-accent: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        VBox rewardBox = new VBox(6, rewardTitle, rewardInfo, progressBar);

        // main content
        VBox content = new VBox(20,
            welcome,
            whatShowing,
            searchBar,
            new Separator(),
            filtersLabel,
            filters,
            new Separator(),
            rewardBox
        );
        content.setPadding(new Insets(40));
        content.setMaxWidth(700);

        HBox contentWrapper = new HBox(content);
        contentWrapper.setAlignment(Pos.TOP_CENTER);

        VBox root = new VBox(navbar, contentWrapper);
        VBox.setVgrow(contentWrapper, Priority.ALWAYS);
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

    private Button filterChip(String text) {
        Button chip = new Button(text);
        chip.setStyle(
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-padding: 6 14 6 14;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 20;" +
            "-fx-background-radius: 20;" +
            "-fx-cursor: hand;"
        );
        return chip;
    }
}