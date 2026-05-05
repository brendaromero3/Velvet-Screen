import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class ClientProfileScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onBrowse;
    private Runnable onPayments;
    private Runnable onRegister;
    private Runnable onLogin;

    public ClientProfileScreen(Stage stage, GuiClient app, Runnable onBack,
                                Runnable onLogout, Runnable onBrowse,
                                Runnable onPayments, Runnable onRegister, Runnable onLogin) {
        this.stage      = stage;
        this.app        = app;
        this.onBack     = onBack;
        this.onLogout   = onLogout;
        this.onBrowse   = onBrowse;
        this.onPayments = onPayments;
        this.onRegister = onRegister;
        this.onLogin    = onLogin;
    }

    public Scene getScene() {

        // Navbar
        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        boolean isGuest = app.isGuest();
        if (!isGuest) {
            wordmark.setOnMouseClicked(e -> {
                if (onBack != null) onBack.run();
            });
        } else {
            wordmark.setOnMouseClicked(e -> {
                if (onBrowse != null) onBrowse.run();
            });
        }

        Button browseBtn   = navButton("Browse");
        Button accountBtn  = navButton("My Account");
        Button paymentsBtn = navButton("Payment Methods");
        Button logoutBtn   = navButton("Logout");

        accountBtn.setStyle(accountBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        browseBtn.setOnAction(e -> { if (onBrowse != null) onBrowse.run(); });
        paymentsBtn.setOnAction(e -> { if (onPayments != null) onPayments.run(); });
        logoutBtn.setOnAction(e -> { if (onLogout != null) onLogout.run(); });

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar;
        if (!isGuest) {
            navbar = new HBox(12, wordmark, navSpacer, browseBtn, accountBtn, paymentsBtn, logoutBtn);
        } else {
            navbar = new HBox(12, wordmark, navSpacer, browseBtn, accountBtn, logoutBtn);
        }

        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        // ── GUEST CHECK ──────────────────────────────────────────────────────
        // If user is not logged in, show a simple register prompt instead of the form

        if (isGuest) {
            Label title = new Label("My Account");
            title.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                "-fx-font-size: 26px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
            );

            Label message = new Label("You are currently browsing as a guest.");
            message.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 14px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
            );

            Label subMessage = new Label("Create an account to save payment methods and earn rewards.");
            subMessage.setStyle(
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
            );

            Button registerBtn = new Button("Create an Account");
            registerBtn.setStyle(
                "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                "-fx-text-fill: white;" +
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 13px;" +
                "-fx-padding: 12 28 12 28;" +
                "-fx-border-radius: 4;" +
                "-fx-background-radius: 4;" +
                "-fx-cursor: hand;"
            );
            
            registerBtn.setOnAction(e -> onRegister.run());

            Button loginBtn = new Button("Already have an account? Login");
            loginBtn.setStyle(
                "-fx-background-color: transparent;" +
                "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
                "-fx-font-family: 'Questrial';" +
                "-fx-font-size: 12px;" +
                "-fx-padding: 0;" +
                "-fx-cursor: hand;" +
                "-fx-border-color: transparent;"
            );
            loginBtn.setOnAction(e -> { if (onLogin != null) onLogin.run(); });

            VBox guestContent = new VBox(16,
                title, message, subMessage,
                new Region() {{ setPrefHeight(8); }},
                registerBtn, loginBtn
            );
            guestContent.setPadding(new Insets(40, 0, 40, 0));
            guestContent.setMaxWidth(500);

            HBox guestWrapper = new HBox(guestContent);
            guestWrapper.setAlignment(Pos.TOP_CENTER);
            guestWrapper.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

            ScrollPane guestScroll = new ScrollPane(guestWrapper);
            guestScroll.setFitToWidth(true);
            guestScroll.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
            guestScroll.setStyle(
                "-fx-background: " + LandingScreen.BG_COLOR + ";" +
                "-fx-background-color: " + LandingScreen.BG_COLOR + ";" +
                "-fx-border-color: transparent;" +
                "-fx-focus-color: transparent;" +
                "-fx-faint-focus-color: transparent;"
            );

            VBox root = new VBox(navbar, guestScroll);
            VBox.setVgrow(guestScroll, Priority.ALWAYS);
            root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");
            return new Scene(root, 1000, 750);
        }

        // ── LOGGED IN VIEW ───────────────────────────────────────────────────
        // Everything below is unchanged — only runs for logged in users

        String[] clientInfo = app.getClientInfo();
        String currentName      = clientInfo[0];
        String currentEmail     = clientInfo[1];
        String currentAddress   = clientInfo[2];
        String currentInterests = clientInfo[3];
        int moviesWatched       = app.getMoviesWatched();

        Label title = new Label("My Account");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label personalSection = new Label("PERSONAL");
        personalSection.setStyle(sectionLabelStyle());

        Label nameLabel = new Label("FULL NAME");
        nameLabel.setStyle(fieldLabelStyle());
        TextField nameField = new TextField(currentName);
        nameField.setStyle(fieldStyle());

        Label emailLabel = new Label("EMAIL");
        emailLabel.setStyle(fieldLabelStyle());
        TextField emailField = new TextField(currentEmail);
        emailField.setStyle(fieldStyle());

        Label addressLabel = new Label("ADDRESS");
        addressLabel.setStyle(fieldLabelStyle());
        TextField addressField = new TextField(currentAddress);
        addressField.setStyle(fieldStyle());

        Label interestsLabel = new Label("MOVIE INTERESTS");
        interestsLabel.setStyle(fieldLabelStyle());
        TextField interestsField = new TextField(currentInterests != null ? currentInterests : "");
        interestsField.setPromptText("Drama, Indie, Foreign Language");
        interestsField.setStyle(fieldStyle());

        Label passwordSection = new Label("CHANGE PASSWORD");
        passwordSection.setStyle(sectionLabelStyle());

        Label currentPassLabel = new Label("CURRENT PASSWORD");
        currentPassLabel.setStyle(fieldLabelStyle());
        PasswordField currentPassField = new PasswordField();
        currentPassField.setPromptText("••••••••");
        currentPassField.setStyle(fieldStyle());

        Label newPassLabel = new Label("NEW PASSWORD");
        newPassLabel.setStyle(fieldLabelStyle());
        PasswordField newPassField = new PasswordField();
        newPassField.setPromptText("••••••••");
        newPassField.setStyle(fieldStyle());

        Label rewardSection = new Label("REWARD PROGRAM");
        rewardSection.setStyle(sectionLabelStyle());

        Label rewardCount = new Label(app.getRewardProgress() + " / 10 tickets to your next free movie.");
        rewardCount.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        VBox rewardBox = new VBox(6);

        boolean hasMem = app.hasMembership();

        if (hasMem) {
            double progress = app.getRewardProgress() / 10.0;
            ProgressBar progressBar = new ProgressBar(progress);
            progressBar.setPrefWidth(400);
            progressBar.setStyle("-fx-accent: " + LandingScreen.SUCCESS_COLOR + ";");

            rewardBox.getChildren().addAll(progressBar);
        } else {
            Button signUpBtn = new Button("Sign Up For Rewards Today!");
            signUpBtn.setStyle(
                    "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Questrial';" +
                            "-fx-font-size: 13px;" +
                            "-fx-padding: 10 20 10 20;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-cursor: hand;"
            );
            signUpBtn.setOnAction(e -> {
                boolean success = app.registerMembership(app.user_id);

                if (success) {
                    stage.setScene(new ClientProfileScreen(stage, app, onBack, onLogout, onBrowse, onPayments, onRegister, onLogin).getScene());
                } else {
                    System.out.println("Failed to register");
                }
            });

            rewardBox.getChildren().addAll(signUpBtn);
        }

        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Label successLabel = new Label("");
        successLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        Button saveBtn = new Button("Save Changes");
        saveBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 28 10 28;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        saveBtn.setOnAction(e -> {
            errorLabel.setText("");
            successLabel.setText("");

            String newName      = nameField.getText().trim();
            String newEmail     = emailField.getText().trim();
            String newAddress   = addressField.getText().trim();
            String newInterests = interestsField.getText().trim();
            String currentPass  = currentPassField.getText().trim();
            String newPass      = newPassField.getText().trim();

            if (newName.isEmpty() || newEmail.isEmpty() || newAddress.isEmpty()) {
                errorLabel.setText("Name, email and address cannot be empty.");
                return;
            }

            if (!currentPass.isEmpty() || !newPass.isEmpty()) {
                if (currentPass.isEmpty()) {
                    errorLabel.setText("Please enter your current password.");
                    return;
                }
                if (newPass.isEmpty()) {
                    errorLabel.setText("Please enter a new password.");
                    return;
                }
                boolean passChanged = app.changePassword(currentPass, newPass);
                if (!passChanged) {
                    errorLabel.setText("Current password is incorrect.");
                    return;
                }
            }

            boolean saved = app.updateClientInfo(newName, newEmail, newAddress, newInterests);
            if (saved) {
                successLabel.setText("Changes saved successfully!");
                currentPassField.clear();
                newPassField.clear();
            } else {
                errorLabel.setText("Failed to save changes. Email may already be in use.");
            }
        });

        VBox form = new VBox(8,
            title,
            new Region() {{ setPrefHeight(16); }},
            personalSection,
            new Separator(),
            nameLabel, nameField,
            new Region() {{ setPrefHeight(4); }},
            emailLabel, emailField,
            new Region() {{ setPrefHeight(4); }},
            addressLabel, addressField,
            new Region() {{ setPrefHeight(4); }},
            interestsLabel, interestsField,
            new Region() {{ setPrefHeight(20); }},
            passwordSection,
            new Separator(),
            currentPassLabel, currentPassField,
            new Region() {{ setPrefHeight(4); }},
            newPassLabel, newPassField,
            new Region() {{ setPrefHeight(20); }},
            rewardSection,
            new Separator(),
            rewardCount,
            rewardBox,
            new Region() {{ setPrefHeight(20); }},
            errorLabel,
            successLabel,
            saveBtn
        );
        form.setMaxWidth(500);
        form.setPadding(new Insets(40, 0, 40, 0));

        HBox formWrapper = new HBox(form);
        formWrapper.setAlignment(Pos.TOP_CENTER);
        formWrapper.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        ScrollPane scroll = new ScrollPane(formWrapper);
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

    private String sectionLabelStyle() {
        return "-fx-font-family: 'Questrial';" +
               "-fx-font-size: 11px;" +
               "-fx-font-weight: bold;" +
               "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";
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
               "-fx-pref-width: 500px;";
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