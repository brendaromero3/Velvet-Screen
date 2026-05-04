import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminPeopleScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onScreenings;
    private Runnable onAnalytics;
    private Runnable onMovies;
    private Runnable onTheaters;
    private Runnable onRegister;

    public AdminPeopleScreen(Stage stage, GuiClient app, Runnable onBack,
                              Runnable onLogout, Runnable onScreenings,
                              Runnable onAnalytics, Runnable onMovies,
                              Runnable onTheaters, Runnable onRegister) {
        this.stage       = stage;
        this.app         = app;
        this.onBack      = onBack;
        this.onLogout    = onLogout;
        this.onScreenings = onScreenings;
        this.onAnalytics = onAnalytics;
        this.onMovies    = onMovies;
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

        peopleBtn.setStyle(peopleBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        dashboardBtn.setOnAction(e ->  { if (onBack != null) onBack.run(); });
        screeningsBtn.setOnAction(e -> { if (onScreenings != null) onScreenings.run(); });
        analyticsBtn.setOnAction(e ->  { if (onAnalytics != null) onAnalytics.run(); });
        moviesBtn.setOnAction(e ->     { if (onMovies != null) onMovies.run(); });
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

        Label title = new Label("People");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button addPersonBtn = new Button("+ Add New Person");
        addPersonBtn.setStyle(
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
        HBox titleRow = new HBox(title, titleSpacer, addPersonBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox personForm = new VBox(10);
        personForm.setVisible(false);
        personForm.setManaged(false);
        personForm.setPadding(new Insets(20));
        personForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label formTitle = new Label("Add New Person");
        formTitle.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label nameLabel = new Label("FULL NAME");
        nameLabel.setStyle(fieldLabelStyle());
        TextField nameField = new TextField();
        nameField.setPromptText("e.g. Celine Song");
        nameField.setStyle(fieldStyle());

        Label birthdateLabel = new Label("BIRTHDATE");
        birthdateLabel.setStyle(fieldLabelStyle());
        DatePicker birthdatePicker = new DatePicker();
        birthdatePicker.setPrefWidth(340);

        Label bioLabel = new Label("BIOGRAPHY");
        bioLabel.setStyle(fieldLabelStyle());
        TextArea bioField = new TextArea();
        bioField.setPromptText("Short biography...");
        bioField.setPrefRowCount(4);
        bioField.setWrapText(true);
        bioField.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 12 10 12;" +
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-pref-width: 340px;"
        );

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

        Button savePersonBtn = new Button("Save Person");
        savePersonBtn.setStyle(
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
            personForm.setVisible(false);
            personForm.setManaged(false);
            formError.setText("");
            formSuccess.setText("");
            nameField.clear();
            bioField.clear();
            birthdatePicker.setValue(null);
        });

        HBox formButtons = new HBox(8, savePersonBtn, cancelFormBtn);

        personForm.getChildren().addAll(
            formTitle,
            nameLabel, nameField,
            birthdateLabel, birthdatePicker,
            bioLabel, bioField,
            formError, formSuccess,
            formButtons
        );

        addPersonBtn.setOnAction(e -> {
            personForm.setVisible(true);
            personForm.setManaged(true);
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

        Label hName      = new Label("NAME");      hName.setStyle(headerStyle);      hName.setPrefWidth(200);
        Label hBirthdate = new Label("BIRTHDATE"); hBirthdate.setStyle(headerStyle); hBirthdate.setPrefWidth(120);
        Label hBio       = new Label("BIO");       hBio.setStyle(headerStyle);       hBio.setPrefWidth(300);
        Label hAwards    = new Label("AWARDS");    hAwards.setStyle(headerStyle);    hAwards.setPrefWidth(80);
        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        Label hActions = new Label("ACTIONS"); hActions.setStyle(headerStyle);

        HBox tableHeader = new HBox(hName, hBirthdate, hBio, hAwards, headerSpacer, hActions);
        tableHeader.setPadding(new Insets(12, 16, 12, 16));
        tableBox.getChildren().add(tableHeader);

        final Runnable[] reloadRef = new Runnable[1];

        reloadRef[0] = () -> {
            tableBox.getChildren().clear();
            tableBox.getChildren().add(tableHeader);

            List<String[]> people = app.getAllPeople();

            if (people.isEmpty()) {
                Label empty = new Label("No people found.");
                empty.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                    "-fx-padding: 16;"
                );
                tableBox.getChildren().add(empty);
                return;
            }

            for (String[] person : people) {
                // person[0]=id, [1]=name, [2]=birthdate, [3]=biography, [4]=award_count
                String bioExcerpt = person[3] != null && person[3].length() > 60 ?
                    person[3].substring(0, 60) + "..." : (person[3] != null ? person[3] : "—");

                String rowStyle =
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";";

                Label rName      = new Label(person[1]);           rName.setStyle(rowStyle);      rName.setPrefWidth(200);
                Label rBirthdate = new Label(person[2] != null ? person[2] : "—");
                                                                   rBirthdate.setStyle(rowStyle); rBirthdate.setPrefWidth(120);
                Label rBio       = new Label(bioExcerpt);          rBio.setStyle(rowStyle);       rBio.setPrefWidth(300);
                Label rAwards    = new Label(person[4] != null ? person[4] : "0");
                                                                   rAwards.setStyle(rowStyle);    rAwards.setPrefWidth(80);

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

                int personId = Integer.parseInt(person[0]);
                deleteBtn.setOnAction(e -> {
                    boolean deleted = app.deletePerson(personId);
                    if (deleted) reloadRef[0].run();
                });

                Region rowSpacer = new Region();
                HBox.setHgrow(rowSpacer, Priority.ALWAYS);
                HBox actions = new HBox(rowSpacer, deleteBtn);
                HBox.setHgrow(actions, Priority.ALWAYS);

                HBox row = new HBox(rName, rBirthdate, rBio, rAwards, actions);
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

        savePersonBtn.setOnAction(e -> {
            formError.setText("");
            formSuccess.setText("");

            String n  = nameField.getText().trim();
            String bio = bioField.getText().trim();

            if (n.isEmpty() || birthdatePicker.getValue() == null) {
                formError.setText("Please fill in name and birthdate.");
                return;
            }

            String birthdate = birthdatePicker.getValue().toString();
            boolean success  = app.addPerson(n, birthdate, bio.isEmpty() ? null : bio);

            if (success) {
                formSuccess.setText("Person added successfully!");
                nameField.clear();
                bioField.clear();
                birthdatePicker.setValue(null);
                reloadRef[0].run();
            } else {
                formError.setText("Failed to add person.");
            }
        });

        VBox content = new VBox(24, titleRow, personForm, tableBox);
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