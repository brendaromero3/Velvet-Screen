import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class AddEditPaymentScreen {

    private Stage stage;
    private GuiClient app;
    private String[] existingCard; 
    private Runnable onSave;
    private Runnable onCancel;

    public AddEditPaymentScreen(Stage stage, GuiClient app, String[] existingCard,
                                 Runnable onSave, Runnable onCancel) {
        this.stage        = stage;
        this.app          = app;
        this.existingCard = existingCard;
        this.onSave       = onSave;
        this.onCancel     = onCancel;
    }

    public Scene getScene() {
        boolean isEditing = existingCard != null;

        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 20px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
            "-fx-cursor: hand;"
        );
        wordmark.setOnMouseClicked(e -> { if (onCancel != null) onCancel.run(); });

        HBox navbar = new HBox(wordmark);
        navbar.setStyle(
            "-fx-padding: 16 40 16 40;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-width: 0 0 1 0;" +
            "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        Label title = new Label(isEditing ? "Edit Payment Method" : "Add Payment Method");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label cardTypeLabel = new Label("CARD TYPE");
        cardTypeLabel.setStyle(fieldLabelStyle());

        ToggleGroup typeGroup = new ToggleGroup();
        ToggleButton creditBtn = new ToggleButton("Credit");
        ToggleButton debitBtn  = new ToggleButton("Debit");
        creditBtn.setToggleGroup(typeGroup);
        debitBtn.setToggleGroup(typeGroup);

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

        Label cardNumberLabel = new Label("CARD NUMBER");
        cardNumberLabel.setStyle(fieldLabelStyle());
        TextField cardNumberField = new TextField();
        cardNumberField.setPromptText("1234 5678 9012 3456");
        cardNumberField.setStyle(fieldStyle());

        Label billingLabel = new Label("BILLING ADDRESS");
        billingLabel.setStyle(fieldLabelStyle());
        TextField billingField = new TextField();
        billingField.setPromptText("12 Park Lane, NYC");
        billingField.setStyle(fieldStyle());

        VBox billingBox = new VBox(4, billingLabel, billingField);

        if (isEditing) {
            boolean isCredit = "Credit".equals(existingCard[1]);
            if (isCredit) {
                creditBtn.setSelected(true);
                creditBtn.setStyle(toggleSelected);
                debitBtn.setStyle(toggleUnselected);
                billingBox.setVisible(true);
                billingBox.setManaged(true);
                if (existingCard[2] != null) billingField.setText(existingCard[2]);
            } else {
                debitBtn.setSelected(true);
                debitBtn.setStyle(toggleSelected);
                creditBtn.setStyle(toggleUnselected);
                billingBox.setVisible(false);
                billingBox.setManaged(false);
            }
            cardNumberField.setText(existingCard[0]);
        } else {
            creditBtn.setSelected(true);
            creditBtn.setStyle(toggleSelected);
            debitBtn.setStyle(toggleUnselected);
            billingBox.setVisible(true);
            billingBox.setManaged(true);
        }

        creditBtn.setOnAction(e -> {
            creditBtn.setStyle(toggleSelected);
            debitBtn.setStyle(toggleUnselected);
            billingBox.setVisible(true);
            billingBox.setManaged(true);
        });
        debitBtn.setOnAction(e -> {
            debitBtn.setStyle(toggleSelected);
            creditBtn.setStyle(toggleUnselected);
            billingBox.setVisible(false);
            billingBox.setManaged(false);
        });

        HBox typeToggle = new HBox(0, creditBtn, debitBtn);

        Label errorLabel = new Label("");
        errorLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 12px;" +
            "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Button saveBtn = new Button(isEditing ? "Save Changes" : "Save");
        saveBtn.setStyle(
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

        saveBtn.setOnAction(e -> {
            String cardNumber = cardNumberField.getText().trim();
            String billing    = creditBtn.isSelected() ? billingField.getText().trim() : null;

            if (cardNumber.isEmpty()) {
                errorLabel.setText("Please enter a card number.");
                return;
            }
            if (creditBtn.isSelected() && (billing == null || billing.isEmpty())) {
                errorLabel.setText("Please enter a billing address for credit cards.");
                return;
            }

            boolean success;
            if (isEditing) {
                String oldBilling = existingCard[2];
                success = app.updatePaymentMethods(cardNumber, billing, oldBilling);
            } else {
                success = app.addPaymentMethod(cardNumber, billing);
            }

            if (success) {
                if (onSave != null) onSave.run();
            } else {
                errorLabel.setText("Failed to save. Card number may already exist.");
            }
        });

        Button cancelBtn = new Button("Cancel");
        cancelBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 0 10 0;" +
            "-fx-pref-width: 340px;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );
        cancelBtn.setOnAction(e -> { if (onCancel != null) onCancel.run(); });

        VBox form = new VBox(8,
            title,
            new Region() {{ setPrefHeight(12); }},
            cardTypeLabel,
            typeToggle,
            new Region() {{ setPrefHeight(8); }},
            cardNumberLabel,
            cardNumberField,
            new Region() {{ setPrefHeight(8); }},
            billingBox,
            new Region() {{ setPrefHeight(4); }},
            errorLabel,
            new Region() {{ setPrefHeight(8); }},
            saveBtn,
            new Region() {{ setPrefHeight(4); }},
            cancelBtn
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