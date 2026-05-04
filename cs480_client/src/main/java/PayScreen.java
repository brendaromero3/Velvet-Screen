import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.layout.GridPane;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.CheckBox;


public class PayScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private String[] movie;
    private int numTickets;
    private double total;
    private String selectedCard;
    private Button currSelected;
    private Label totalCost = new Label();
    private boolean useFree;
    private int numFreeToUse;

    public PayScreen(Stage stage, GuiClient app, Runnable onBack, String[] movie, int numTickets, double total, boolean useFree, int numFreeToUse) {
        this.stage   = stage;
        this.app     = app;
        this.onBack  = onBack;
        this.movie = movie;
        this.numTickets = numTickets;
        this.total = total;
        this.useFree = useFree;
        this.numFreeToUse = numFreeToUse;
    }




    public Scene getScene() {
        // navbar
        Label wordmark = new Label("Velvet Screen");
        wordmark.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                        "-fx-font-size: 20px;" +
                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";" +
                        "-fx-cursor: hand;"
        );
        wordmark.setOnMouseClicked(e -> { if (onBack != null) onBack.run(); });

        Button backBtn   = navButton("Back");
        backBtn.setOnAction(e -> onBack.run());

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer, backBtn);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
                "-fx-padding: 16 40 16 40;" +
                        "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;" +
                        "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        boolean isGuest = app.isGuest();
        VBox content;

        if (isGuest) {
            content  = guestPay();
        }
        else {
            content = userPay();
        }

        VBox root = new VBox(navbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");

        return new Scene(root, 1000, 750);
    }

    private void bookTickets(String cardNumber) {
        int screeningId = Integer.parseInt(movie[5]);

        int usedFree = 0;

        if (useFree) {
            usedFree = app.freeTickets(numFreeToUse);
        }

        double ticketPrice = total / numTickets;
        double finalTotal = (numTickets - usedFree) * ticketPrice;

        totalCost.setText("$" + finalTotal);

        Integer bookingId = app.book(screeningId, numTickets, finalTotal, cardNumber);

        if (bookingId != null) {
            stage.setScene(new ConfirmScreen(stage, app, onBack, movie, numTickets, finalTotal, bookingId).getScene());
        } else {
            System.out.println("Booking failed");
        }
    }

    public VBox userPay() {
        Button confirmBtn = new Button("Confirm Payment");
        confirmBtn.setDisable(true);
        confirmBtn.setStyle(
                "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Questrial';" +
                        "-fx-font-size: 13px;" +
                        "-fx-padding: 10 20 10 20;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );

        confirmBtn.setOnAction(e -> {
            if (selectedCard == null) {
                return;
            }

            bookTickets(selectedCard);
        });

        // Cards list
        VBox cardsBox = new VBox(8);
        cardsBox.setFillWidth(true);

        final Runnable[] loadCardsRef = new Runnable[1];

        loadCardsRef[0] = () -> {
            cardsBox.getChildren().clear();

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
                    cardsBox.getChildren().add(cardRow(card, loadCardsRef, confirmBtn));
                }
            }
        };

        loadCardsRef[0].run();

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

        HBox summary = orderSummary(addBtn);

        VBox resultsBox = new VBox(8);
        resultsBox.setFillWidth(true);

        ScrollPane scroll = new ScrollPane(resultsBox);
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

        VBox content = new VBox(30, summary, cardsBox, confirmBtn, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);
        content.setAlignment(Pos.CENTER);

        return content;
    }

    public VBox guestPay() {
        Label ticketsLbl = new Label("Number of Tickets");
        ticketsLbl.setStyle(bodyTextStyle(20));

        Label ticketCost = new Label(String.valueOf(numTickets));
        ticketCost.setStyle(bodyTextStyle(20));

        VBox ticketBox = new VBox(4, ticketsLbl, ticketCost);

        Label totalLbl = new Label("Total");
        totalLbl.setStyle(bodyTextStyle(20));

        totalCost.setText("$" + total);
        totalCost.setStyle(bodyTextStyle(20));

        VBox totalBox = new VBox(4, totalLbl, totalCost);

        HBox sum = new HBox(100, ticketBox, totalBox);
        sum.setAlignment(Pos.CENTER);

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

        creditBtn.setSelected(true);
        creditBtn.setStyle(toggleSelected);
        debitBtn.setStyle(toggleUnselected);
        billingBox.setVisible(true);
        billingBox.setManaged(true);

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

        Label errorLabel = new Label("");
        errorLabel.setStyle(
                "-fx-font-family: 'Questrial';" +
                        "-fx-font-size: 12px;" +
                        "-fx-text-fill: " + LandingScreen.ERROR_COLOR + ";"
        );

        Button confirmBtn = new Button("Confirm");
        confirmBtn.setStyle(
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

        confirmBtn.setOnAction(e -> {
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

            bookTickets(cardNumber);

        });

        HBox typeToggle = new HBox(0, creditBtn, debitBtn);

        VBox form = new VBox(8,
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
                confirmBtn
        );
        form.setAlignment(Pos.TOP_LEFT);
        form.setMaxWidth(340);
        form.setPadding(new Insets(40, 0, 40, 0));

        HBox formWrapper = new HBox(form);
        formWrapper.setAlignment(Pos.CENTER);
        formWrapper.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");


        VBox resultsBox = new VBox(8);
        resultsBox.setFillWidth(true);

        ScrollPane scroll = new ScrollPane(resultsBox);
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

        VBox content = new VBox(30, sum, formWrapper, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);
        content.setAlignment(Pos.CENTER);

        return content;
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

    private String bodyTextStyle(int size) {
        return  "-fx-font-family: 'Questrial';" +
                "-fx-font-size: " + size + "px;" +
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
                "-fx-pref-width: 340px;";
    }

    private HBox orderSummary(Button addBtn) {
        Label ticketsLbl = new Label("Number of Tickets");
        ticketsLbl.setStyle(bodyTextStyle(20));

        Label ticketCost = new Label(String.valueOf(numTickets));
        ticketCost.setStyle(bodyTextStyle(20));

        VBox ticketBox = new VBox(4, ticketsLbl, ticketCost);

        Label totalLbl = new Label("Total");
        totalLbl.setStyle(bodyTextStyle(20));

        totalCost.setText("$" + total);
        totalCost.setStyle(bodyTextStyle(20));

        VBox totalBox = new VBox(4, totalLbl, totalCost);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);


        HBox sum = new HBox(100, ticketBox, totalBox, spacer, addBtn);
        sum.setAlignment(Pos.CENTER);

        return sum;
    }

    private HBox cardRow(String[] card, Runnable[] loadCardsRef, Button confirmBtn) {
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

        Button selectBtn = new Button("Select");
        selectBtn.setStyle(
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
        selectBtn.setOnAction(e -> {
            selectedCard = card[0];
//            userId = Integer.parseInt(card[1]);
            confirmBtn.setDisable(false);

            if (currSelected != null) {
                currSelected.setStyle(
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
            }

            selectBtn.setStyle(
                    "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Questrial';" +
                            "-fx-font-size: 12px;" +
                            "-fx-padding: 4 10 4 10;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-cursor: hand;"
            );

            currSelected = selectBtn;
        });

        HBox actions = new HBox(6, selectBtn);
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
}