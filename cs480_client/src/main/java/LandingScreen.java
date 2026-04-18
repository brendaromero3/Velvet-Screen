import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;


public class LandingScreen {

    // colors
    public static final String BG_COLOR       = "#F5F0EA"; // warm white
    public static final String PRIMARY_COLOR  = "#9b2335"; // cardinal red
    public static final String TEXT_DARK      = "#1a1a1a"; // near black
    public static final String TEXT_MUTED     = "#6b6b6b"; // medium gray
    public static final String BORDER_COLOR   = "#e0d8d0"; // light warm gray
    public static final String WHITE         = "#ffffff";  // bright white
    public static final String ERROR_COLOR   = "#c0392b";  // bright red
    public static final String PRIMARY_HOVER = "#7a1c2a";  // darker cardinal (hover)
    public static final String SUCCESS_COLOR = "#2d8a6e";  // jade green

    private Stage stage;
    private Runnable onLogin;
    private Runnable onRegister;

    public LandingScreen(Stage stage, Runnable onLogin, Runnable onRegister) {
        this.stage    = stage;
        this.onLogin  = onLogin;
        this.onRegister = onRegister;
    }

    public Scene getScene() {
        Label title = new Label("Velvet Screen");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 50px;" +
            "-fx-text-fill: " + TEXT_DARK + ";"
        );
        Label est = new Label("CS 480 FALL 2026 - GROUP 1");
        est.setStyle(
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + PRIMARY_COLOR + ";" +
            "-fx-letter-spacing: 0.15em;" +
            "-fx-font-family: 'Questrial';" 
        );

        Label sub = new Label("Discover screenings, book seats, and earn rewards.");
        sub.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-style: italic;" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + TEXT_MUTED + ";"
        );

        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
            "-fx-background-color: " + PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 28 10 28;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        loginBtn.setOnAction(e -> onLogin.run());

        Button registerBtn = new Button("Register");
        registerBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + PRIMARY_COLOR + ";" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 28 10 28;" +
            "-fx-border-color: " + PRIMARY_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        registerBtn.setOnAction(e -> onRegister.run());

        HBox buttons = new HBox(12, loginBtn, registerBtn);
        buttons.setAlignment(Pos.CENTER);

        Label footer = new Label("Brought to you by: Brenda, Lena, and Ashley :)");
        footer.setStyle(
            "-fx-font-family: 'Questrial';" + 
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + TEXT_DARK + ";"
        );

        Region topSpacer = new Region();
        topSpacer.setPrefHeight(80);
        Region midSpacer1 = new Region();
        midSpacer1.setPrefHeight(24);
        Region midSpacer2 = new Region();
        midSpacer2.setPrefHeight(32);
        Region bottomSpacer = new Region();
        bottomSpacer.setPrefHeight(60);

        VBox root = new VBox(
            topSpacer,
            title,
            est,
            midSpacer1,
            sub,
            midSpacer2,
            buttons,
            bottomSpacer,
            footer
        );
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: " + BG_COLOR + ";");

        return new Scene(root, 900, 650);
    }
}