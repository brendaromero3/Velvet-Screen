import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class LoginScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onRegister;

    public LoginScreen(Stage stage, GuiClient app, Runnable onBack, Runnable onRegister) {
        this.stage      = stage;
        this.app        = app;
        this.onBack     = onBack;
        this.onRegister = onRegister;
    }

    public Scene getScene() {

        Label titleCorner = new Label("Velvet Screen");
        titleCorner.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        titleCorner.setOnMouseClicked(e -> {
            if (onBack != null) onBack.run();
        });

        HBox navbar = new HBox(titleCorner);
        navbar.setStyle(
            "-fx-padding: 20 40 20 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        // title
        Label title = new Label("Welcome back.");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label subtitle = new Label("Sign in to continue.");
        subtitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        // email
        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );
        TextField emailField = new TextField();
        emailField.setPromptText("you@example.com");
        emailField.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 12 10 12;" +
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-pref-width: 340px;"
        );

        // password
        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 11px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 12 10 12;" +
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-pref-width: 340px;"
        );

        // error label
        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        // login button
        Button loginBtn = new Button("Login");
        loginBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 0 10 0;" +
            "-fx-pref-width: 340px;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        loginBtn.setOnAction(e -> {
            String email    = emailField.getText().trim();
            String password = passwordField.getText().trim();
            if (email.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please enter your email and password.");
                return;
            }
            boolean success = app.logIn(email, password, stage);
            if (!success) {
                errorLabel.setText("Incorrect email or password. Please try again.");
            }
        });

        // register link
        Label registerLink = new Label("New here? Create an account");
        registerLink.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-cursor: hand;"
        );
        registerLink.setOnMouseClicked(e -> onRegister.run());

        // form
        VBox form = new VBox(8,
            title,
            subtitle,
            new Region() {{ setPrefHeight(16); }},
            emailLabel,
            emailField,
            new Region() {{ setPrefHeight(8); }},
            passwordLabel,
            passwordField,
            new Region() {{ setPrefHeight(4); }},
            errorLabel,
            new Region() {{ setPrefHeight(8); }},
            loginBtn,
            new Region() {{ setPrefHeight(12); }},
            registerLink
        );
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(340);

        VBox center = new VBox(form);
        center.setAlignment(Pos.CENTER);
        VBox.setVgrow(center, Priority.ALWAYS);

        VBox root = new VBox(navbar, center);
        VBox.setVgrow(center, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        return new Scene(root, 1000, 750);
    }
}