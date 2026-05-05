import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class RegisterScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogin;
    private Runnable onCreate;

    public RegisterScreen(Stage stage, GuiClient app, Runnable onBack, Runnable onLogin, Runnable onCreate) {
        this.stage   = stage;
        this.app     = app;
        this.onBack  = onBack;
        this.onLogin = onLogin;
        this.onCreate = onCreate;
    }

    public Scene getScene(boolean admin) {

        // navbar
        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        wordmark.setOnMouseClicked(e -> { if (onBack != null) onBack.run(); });

        HBox navbar = new HBox(wordmark);
        navbar.setStyle(
            "-fx-padding: 20 40 20 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        // title
        Label title = new Label("Become a member.");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 28px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label subtitle = new Label("Join the program. Earn a free ticket every ten visits.");
        subtitle.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        // client/admin toggle
        Label registeringAs = new Label("REGISTERING AS");
        registeringAs.setStyle(fieldLabelStyle());

        ToggleGroup roleGroup = new ToggleGroup();
        ToggleButton clientBtn = new ToggleButton("Client");
        ToggleButton adminBtn  = new ToggleButton("Admin");
        
        // checks if addmin, then allows admin button to be pressed
        clientBtn.setToggleGroup(roleGroup);
        if(admin) {
            adminBtn.setToggleGroup(roleGroup);
        }

        clientBtn.setSelected(true);

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

        clientBtn.setStyle(toggleSelected);
        adminBtn.setStyle(toggleUnselected);

        // toggle logic
        clientBtn.setOnAction(e -> {
            clientBtn.setStyle(toggleSelected);
            adminBtn.setStyle(toggleUnselected);
        });
        adminBtn.setOnAction(e -> {
            adminBtn.setStyle(toggleSelected);
            clientBtn.setStyle(toggleUnselected);
        });

        HBox roleToggle;
        if(admin) {
            roleToggle = new HBox(0, clientBtn, adminBtn);
        }
        else {
            roleToggle = new HBox(0, clientBtn);
        }

        // fields
        Label nameLabel = new Label("FULL NAME");
        nameLabel.setStyle(fieldLabelStyle());
        TextField nameField = new TextField();
        nameField.setPromptText("Eleanor Hayes");
        nameField.setStyle(fieldStyle());

        Label emailLabel = new Label("EMAIL ADDRESS");
        emailLabel.setStyle(fieldLabelStyle());
        TextField emailField = new TextField();
        emailField.setPromptText("eleanor@mail.com");
        emailField.setStyle(fieldStyle());

        Label addressLabel = new Label("ADDRESS");
        addressLabel.setStyle(fieldLabelStyle());
        TextField addressField = new TextField();
        addressField.setPromptText("12 Park Lane, NYC");
        addressField.setStyle(fieldStyle());

        Label passwordLabel = new Label("PASSWORD");
        passwordLabel.setStyle(fieldLabelStyle());
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("••••••••");
        passwordField.setStyle(fieldStyle());

        Label interestsLabel = new Label("MOVIE INTERESTS");
        interestsLabel.setStyle(fieldLabelStyle());
        TextField interestsField = new TextField();
        interestsField.setPromptText("Drama, Indie, Foreign Language");
        interestsField.setStyle(fieldStyle());

        // error label
        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        // register button
        Button registerBtn = new Button("Create Account");
        registerBtn.setStyle(
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

        // register button logic
        registerBtn.setOnAction(e -> {
            String name      = nameField.getText().trim();
            String email     = emailField.getText().trim();
            String address   = addressField.getText().trim();
            String password  = passwordField.getText().trim();
            String interests = interestsField.getText().trim();
            boolean isAdmin  = adminBtn.isSelected();

            if (name.isEmpty() || email.isEmpty() || address.isEmpty() || password.isEmpty()) {
                errorLabel.setText("Please fill in all required fields.");
                return;
            }

            boolean success = app.register(name, email, address, password, interests, isAdmin);
            if (success) {
                if(admin) {
                    if (onCreate != null) onCreate.run();
                } else {
                    if (onLogin != null) onLogin.run();
                }
            } else {
                errorLabel.setText("Registration failed. Email may already be in use.");
            }
        });

        // login link
        Label loginLink = new Label("Already have an account? Login");
        loginLink.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-cursor: hand;"
        );
        loginLink.setOnMouseClicked(e -> { if (onLogin != null) onLogin.run(); });

        // form
        VBox form = new VBox(8,
            title,
            subtitle,
            new Region() {{ setPrefHeight(12); }},
            registeringAs,
            roleToggle,
            new Region() {{ setPrefHeight(8); }},
            nameLabel, nameField,
            new Region() {{ setPrefHeight(4); }},
            emailLabel, emailField,
            new Region() {{ setPrefHeight(4); }},
            addressLabel, addressField,
            new Region() {{ setPrefHeight(4); }},
            passwordLabel, passwordField,
            new Region() {{ setPrefHeight(4); }},
            interestsLabel, interestsField,
            new Region() {{ setPrefHeight(4); }},
            errorLabel,
            new Region() {{ setPrefHeight(8); }},
            registerBtn,
            new Region() {{ setPrefHeight(12); }},
            loginLink
        );
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(340);
        form.setPadding(new Insets(40, 0, 40, 0));

        HBox formWrapper = new HBox(form);
        formWrapper.setAlignment(Pos.CENTER);
        formWrapper.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        ScrollPane scroll = new ScrollPane(formWrapper);
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
}