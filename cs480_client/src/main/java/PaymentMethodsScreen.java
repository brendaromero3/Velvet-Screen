import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.List;
import javafx.scene.layout.StackPane;

public class PaymentMethodsScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private Runnable onBrowse;
    private Runnable onAccount;

    public PaymentMethodsScreen(Stage stage, GuiClient app, Runnable onBack,
                                 Runnable onLogout, Runnable onBrowse, Runnable onAccount) {
        this.stage     = stage;
        this.app       = app;
        this.onBack    = onBack;
        this.onLogout  = onLogout;
        this.onBrowse  = onBrowse;
        this.onAccount = onAccount;
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
        wordmark.setOnMouseClicked(e -> { if (onBack != null) onBack.run(); });

        Button browseBtn   = navButton("Browse");
        Button accountBtn  = navButton("My Account");
        Button paymentsBtn = navButton("Payment Methods");
        Button logoutBtn   = navButton("Logout");

        paymentsBtn.setStyle(paymentsBtn.getStyle() +
            "-fx-border-color: transparent transparent " + LandingScreen.PRIMARY_COLOR + " transparent;" +
            "-fx-border-width: 0 0 2 0;");

        browseBtn.setOnAction(e -> { if (onBrowse != null) onBrowse.run(); });
        accountBtn.setOnAction(e -> { if (onAccount != null) onAccount.run(); });
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

        // Title
        Label title = new Label("Payment Methods");
        title.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 26px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        // Cards list
        VBox cardsBox = new VBox(8);
        cardsBox.setFillWidth(true);

        // Confirm delete overlay
        VBox confirmOverlay = new VBox(12);
        confirmOverlay.setAlignment(Pos.CENTER);
        confirmOverlay.setPadding(new Insets(24));
        confirmOverlay.setMaxWidth(360);
        confirmOverlay.setStyle(
            "-fx-background-color: white;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;" +
            "-fx-effect: dropshadow(gaussian, rgba(0,0,0,0.12), 12, 0, 0, 4);"
        );
        confirmOverlay.setVisible(false);
        confirmOverlay.setManaged(false);

        Label confirmTitle = new Label("Delete card?");
        confirmTitle.setStyle(
            "-fx-font-family: 'Noto Serif Display';" +
            "-fx-font-size: 18px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        Label confirmMsg = new Label("This action cannot be undone.");
        confirmMsg.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );

        Button confirmDeleteBtn = new Button("Yes, delete");
        confirmDeleteBtn.setStyle(
            "-fx-background-color: " + LandingScreen.ERROR_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        Button cancelDeleteBtn = new Button("Cancel");
        cancelDeleteBtn.setStyle(
            "-fx-background-color: transparent;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 8 20 8 20;" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        cancelDeleteBtn.setOnAction(e -> {
            confirmOverlay.setVisible(false);
            confirmOverlay.setManaged(false);
        });

        HBox confirmButtons = new HBox(8, confirmDeleteBtn, cancelDeleteBtn);
        confirmButtons.setAlignment(Pos.CENTER);
        confirmOverlay.getChildren().addAll(confirmTitle, confirmMsg, confirmButtons);

        final Runnable[] loadCardsRef = new Runnable[1];
        final String[][] pendingDelete = new String[1][];

        confirmDeleteBtn.setOnAction(e -> {
            if (pendingDelete[0] != null) {
                String cardNumber  = pendingDelete[0][0];
                String billingAddr = pendingDelete[0][2];
                app.deletePaymentMethods(cardNumber, "—".equals(billingAddr) ? null : billingAddr);
                pendingDelete[0] = null;
                confirmOverlay.setVisible(false);
                confirmOverlay.setManaged(false);
                if (loadCardsRef[0] != null) loadCardsRef[0].run();
            }
        });

        Button addBtn = new Button("+ Add Payment Method");
        addBtn.setStyle(
            "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
            "-fx-text-fill: white;" +
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-padding: 10 20 10 20;" +
            "-fx-border-radius: 4;" +
            "-fx-background-radius: 4;" +
            "-fx-cursor: hand;"
        );

        addBtn.setOnAction(e -> {
            stage.setScene(new AddEditPaymentScreen(
                stage, app, null,
                () -> {
                    stage.setScene(getScene());
                    stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                },
                () -> {
                    stage.setScene(getScene());
                    stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                }
            ).getScene());
            stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        });

        loadCardsRef[0] = () -> {
            cardsBox.getChildren().clear();
            List<String[]> cards = app.getPaymentMethods();

            if (cards.isEmpty()) {
                Label noCards = new Label("No payment methods saved yet.");
                noCards.setStyle(
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 13px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
                );
                cardsBox.getChildren().add(noCards);
            } else {
                String headerStyle =
                    "-fx-font-family: 'Questrial';" +
                    "-fx-font-size: 11px;" +
                    "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";

                Label hType    = new Label("TYPE");
                Label hNumber  = new Label("CARD NUMBER");
                Label hAddr    = new Label("BILLING ADDRESS");
                Label hActions = new Label("ACTIONS");

                hType.setStyle(headerStyle);    hType.setPrefWidth(120);
                hNumber.setStyle(headerStyle);  hNumber.setPrefWidth(220);
                hAddr.setStyle(headerStyle);    hAddr.setPrefWidth(300);
                hActions.setStyle(headerStyle); hActions.setPrefWidth(150);

                HBox header = new HBox(hType, hNumber, hAddr, hActions);
                header.setPadding(new Insets(8, 16, 8, 16));
                cardsBox.getChildren().add(header);

                for (String[] card : cards) {
                    cardsBox.getChildren().add(cardRow(card, loadCardsRef, pendingDelete, confirmOverlay));
                }
            }
        };

        loadCardsRef[0].run();

        Region titleSpacer = new Region();
        HBox.setHgrow(titleSpacer, Priority.ALWAYS);
        HBox titleRow = new HBox(title, titleSpacer, addBtn);
        titleRow.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(24, titleRow, cardsBox);
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

        confirmOverlay.setAlignment(Pos.CENTER);
        StackPane stackRoot = new StackPane(root, confirmOverlay);
        confirmOverlay.setMaxWidth(360);
        confirmOverlay.setMaxHeight(200);
        StackPane.setAlignment(confirmOverlay, Pos.CENTER);

        return new Scene(stackRoot, 1000, 750);
    }

    private HBox cardRow(String[] card, Runnable[] loadCardsRef,
                          String[][] pendingDelete, VBox confirmOverlay) {
        String cardNumber  = card[0];
        String cardType    = card[1] != null ? card[1] : "Debit";
        String billingAddr = card[2] != null ? card[2] : "—";

        String masked = "•••• •••• •••• " +
            (cardNumber.length() >= 4 ? cardNumber.substring(cardNumber.length() - 4) : cardNumber);

        Label typeLabel = new Label(cardType);
        typeLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );
        typeLabel.setPrefWidth(120);

        Label numberLabel = new Label(masked);
        numberLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );
        numberLabel.setPrefWidth(220);

        Label addrLabel = new Label(billingAddr);
        addrLabel.setStyle(
            "-fx-font-family: 'Questrial';" +
            "-fx-font-size: 13px;" +
            "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";"
        );
        addrLabel.setPrefWidth(300);

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
        editBtn.setOnAction(e -> {
            stage.setScene(new AddEditPaymentScreen(
                stage, app, card,
                () -> {
                    stage.setScene(getScene());
                    stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                },
                () -> {
                    stage.setScene(getScene());
                    stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
                }
            ).getScene());
            stage.getScene().setFill(javafx.scene.paint.Color.web(LandingScreen.BG_COLOR));
        });

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
        deleteBtn.setOnAction(e -> {
            pendingDelete[0] = card;
            confirmOverlay.setVisible(true);
            confirmOverlay.setManaged(true);
        });

        HBox actions = new HBox(6, editBtn, deleteBtn);
        actions.setPrefWidth(150);

        HBox row = new HBox(typeLabel, numberLabel, addrLabel, actions);
        row.setAlignment(Pos.CENTER_LEFT);
        row.setPadding(new Insets(14, 16, 14, 16));
        row.setStyle(
            "-fx-background-color: " + LandingScreen.WHITE + ";" +
            "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
            "-fx-border-radius: 8;" +
            "-fx-background-radius: 8;"
        );

        return row;
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