import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;

public class AdminTheatersScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onScreenings;
    private Runnable onAnalytics;
    private Runnable onMovies;
    private Runnable onPeople;

    public AdminTheatersScreen(Stage stage, GuiClient app, Runnable onBack,
                                Runnable onLogout, Runnable onScreenings,
                                Runnable onAnalytics, Runnable onMovies, Runnable onPeople) {
        this.stage       = stage;
        this.app         = app;
        this.onBack      = onBack;
        this.onLogout    = onLogout;
        this.onScreenings = onScreenings;
        this.onAnalytics = onAnalytics;
        this.onMovies    = onMovies;
        this.onPeople    = onPeople;
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
        Button logoutBtn     = navButton("Logout");

        theatersBtn.setStyle(theatersBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        dashboardBtn.setOnAction(e ->  { if (onBack != null) onBack.run(); });
        screeningsBtn.setOnAction(e -> { if (onScreenings != null) onScreenings.run(); });
        analyticsBtn.setOnAction(e ->  { if (onAnalytics != null) onAnalytics.run(); });
        moviesBtn.setOnAction(e ->     { if (onMovies != null) onMovies.run(); });
        peopleBtn.setOnAction(e ->     { if (onPeople != null) onPeople.run(); });
        logoutBtn.setOnAction(e ->     { if (onLogout != null) onLogout.run(); });

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer,
            dashboardBtn, screeningsBtn, analyticsBtn, moviesBtn, peopleBtn, theatersBtn, logoutBtn);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        Label title = new Label("Theaters");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Button addTheaterBtn = new Button("+ Add Theater");
        addTheaterBtn.setStyle(
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
        HBox titleRow = new HBox(title, titleSpacer, addTheaterBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox addForm = new VBox(10);
        addForm.setVisible(false);
        addForm.setManaged(false);
        addForm.setPadding(new Insets(20));
        addForm.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        Label addFormTitle = new Label("Add New Theater");
        addFormTitle.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label addCapLabel = new Label("CAPACITY");
        addCapLabel.setStyle(fieldLabelStyle());
        TextField addCapField = new TextField();
        addCapField.setPromptText("e.g. 150");
        addCapField.setStyle(fieldStyle());

        Label add3dLabel = new Label("3D");
        add3dLabel.setStyle(fieldLabelStyle());
        CheckBox add3dBox = new CheckBox("Has 3D");
        add3dBox.setStyle("-fx-font-family: 'Questrial'; -fx-font-size: 13px;");

        Label addSoundLabel = new Label("FANCY SOUND");
        addSoundLabel.setStyle(fieldLabelStyle());
        CheckBox addSoundBox = new CheckBox("Has Fancy Sound");
        addSoundBox.setStyle("-fx-font-family: 'Questrial'; -fx-font-size: 13px;");

        Label addError = new Label("");
        addError.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Label addSuccess = new Label("");
        addSuccess.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.SUCCESS_COLOR + ";"
        );

        Button saveAddBtn = new Button("Save Theater");
        saveAddBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        Button cancelAddBtn = new Button("Cancel");
        cancelAddBtn.setStyle(
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

        cancelAddBtn.setOnAction(e -> {
            addForm.setVisible(false);
            addForm.setManaged(false);
            addCapField.clear();
            add3dBox.setSelected(false);
            addSoundBox.setSelected(false);
            addError.setText("");
            addSuccess.setText("");
        });

        addForm.getChildren().addAll(
            addFormTitle,
            addCapLabel, addCapField,
            add3dLabel, add3dBox,
            addSoundLabel, addSoundBox,
            addError, addSuccess,
            new HBox(8, saveAddBtn, cancelAddBtn)
        );

        addTheaterBtn.setOnAction(e -> {
            addForm.setVisible(true);
            addForm.setManaged(true);
        });

        final Runnable[] reloadRef = new Runnable[1];
        final int[]      editingId = {-1};

        Label editCapLabel = new Label("CAPACITY");
        editCapLabel.setStyle(fieldLabelStyle());
        TextField editCapField = new TextField();
        editCapField.setStyle(fieldStyle());

        Label edit3dLabel = new Label("3D");
        edit3dLabel.setStyle(fieldLabelStyle());
        CheckBox edit3dBox = new CheckBox("Has 3D");
        edit3dBox.setStyle("-fx-font-family: 'Questrial'; -fx-font-size: 13px;");

        Label editSoundLabel = new Label("FANCY SOUND");
        editSoundLabel.setStyle(fieldLabelStyle());
        CheckBox editSoundBox = new CheckBox("Has Fancy Sound");
        editSoundBox.setStyle("-fx-font-family: 'Questrial'; -fx-font-size: 13px;");

        Label editError = new Label("");
        editError.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Button saveEditBtn = new Button("Save Changes");
        saveEditBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8 16 8 16;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        Button cancelEditBtn = new Button("Cancel");
        cancelEditBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8 16 8 16;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        VBox inlineEditBox = new VBox(10);
        inlineEditBox.setVisible(false);
        inlineEditBox.setManaged(false);
        inlineEditBox.setPadding(new Insets(16));
        inlineEditBox.setStyle(
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";" +
            "-fx-border-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-border-width: 0 0 0 3;"
        );

        cancelEditBtn.setOnAction(e -> {
            inlineEditBox.setVisible(false);
            inlineEditBox.setManaged(false);
            editingId[0] = -1;
            editError.setText("");
        });

        saveEditBtn.setOnAction(e -> {
            editError.setText("");
            String capStr = editCapField.getText().trim();
            if (capStr.isEmpty()) {
                editError.setText("Capacity cannot be empty.");
                return;
            }
            int cap;
            try {
                cap = Integer.parseInt(capStr);
            } catch (NumberFormatException ex) {
                editError.setText("Capacity must be a number.");
                return;
            }
            boolean success = app.updateTheater(editingId[0], cap, edit3dBox.isSelected(), editSoundBox.isSelected());
            if (success) {
                inlineEditBox.setVisible(false);
                inlineEditBox.setManaged(false);
                editingId[0] = -1;
                reloadRef[0].run();
            } else {
                editError.setText("Failed to save changes.");
            }
        });

        inlineEditBox.getChildren().addAll(
            editCapLabel, editCapField,
            edit3dLabel, edit3dBox,
            editSoundLabel, editSoundBox,
            editError,
            new HBox(8, saveEditBtn, cancelEditBtn)
        );

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

        Label hId      = new Label("ID");       hId.setStyle(headerStyle);      hId.setPrefWidth(60);
        Label hCap     = new Label("CAPACITY"); hCap.setStyle(headerStyle);     hCap.setPrefWidth(100);
        Label h3d      = new Label("3D");       h3d.setStyle(headerStyle);      h3d.setPrefWidth(80);
        Label hSound   = new Label("SOUND");    hSound.setStyle(headerStyle);   hSound.setPrefWidth(80);

        Region headerSpacer = new Region();
        HBox.setHgrow(headerSpacer, Priority.ALWAYS);
        Label hActions = new Label("ACTIONS");
        hActions.setStyle(headerStyle);

        HBox tableHeader = new HBox(hId, hCap, h3d, hSound, headerSpacer, hActions);
        tableHeader.setPadding(new Insets(12, 16, 12, 16));
        tableBox.getChildren().add(tableHeader);
        tableBox.getChildren().add(inlineEditBox);

        reloadRef[0] = () -> {
            tableBox.getChildren().clear();
            tableBox.getChildren().add(tableHeader);
            tableBox.getChildren().add(inlineEditBox);

            List<String[]> theaters = app.getAllTheaters();

            if (theaters.isEmpty()) {
                Label empty = new Label("No theaters found.");
                empty.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
                    "-fx-padding: 16;"
                );
                tableBox.getChildren().add(empty);
                return;
            }

            for (String[] theater : theaters) {
                String is3dStr  = "t".equals(theater[2]) ? "Yes" : "No";
                String soundStr = "t".equals(theater[3]) ? "Yes" : "No";

                String rowStyle =
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";";

                Label rId    = new Label(theater[0]); rId.setStyle(rowStyle);    rId.setPrefWidth(60);
                Label rCap   = new Label(theater[1]); rCap.setStyle(rowStyle);   rCap.setPrefWidth(100);
                Label r3d    = new Label(is3dStr);    r3d.setStyle(rowStyle);    r3d.setPrefWidth(80);
                Label rSound = new Label(soundStr);   rSound.setStyle(rowStyle); rSound.setPrefWidth(80);

                Button editBtn = new Button("Edit");
                editBtn.setStyle(
                    "-fx-background-color: transparent;" +
                    "-fx-text-fill: " + LandingScreen.PRIMARY_COLOR + ";" +
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 12px;" +
                    "-fx-padding: 4 10 4 10;" +
                    "-fx-border-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                    "-fx-border-radius: 4;" +
                    "-fx-background-radius: 4;" +
                    "-fx-cursor: hand;"
                );

                int theaterId = Integer.parseInt(theater[0]);
                editBtn.setOnAction(e -> {
                    editingId[0] = theaterId;
                    editCapField.setText(theater[1]);
                    edit3dBox.setSelected("t".equals(theater[2]));
                    editSoundBox.setSelected("t".equals(theater[3]));
                    editError.setText("");
                    inlineEditBox.setVisible(true);
                    inlineEditBox.setManaged(true);
                });

                Region rowSpacer = new Region();
                HBox.setHgrow(rowSpacer, Priority.ALWAYS);
                HBox actions = new HBox(rowSpacer, editBtn);
                HBox.setHgrow(actions, Priority.ALWAYS);

                HBox row = new HBox(rId, rCap, r3d, rSound, actions);
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

        saveAddBtn.setOnAction(e -> {
            addError.setText("");
            addSuccess.setText("");
            String capStr = addCapField.getText().trim();
            if (capStr.isEmpty()) {
                addError.setText("Please enter a capacity.");
                return;
            }
            int cap;
            try {
                cap = Integer.parseInt(capStr);
            } catch (NumberFormatException ex) {
                addError.setText("Capacity must be a number.");
                return;
            }
            boolean success = app.addTheater(cap, add3dBox.isSelected(), addSoundBox.isSelected());
            if (success) {
                addSuccess.setText("Theater added successfully!");
                addCapField.clear();
                add3dBox.setSelected(false);
                addSoundBox.setSelected(false);
                reloadRef[0].run();
            } else {
                addError.setText("Failed to add theater.");
            }
        });

        VBox content = new VBox(24, titleRow, addForm, tableBox);
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