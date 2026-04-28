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

public class PayScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private String[] movie;
    private int numTickets;
    private double total;
    private String selectedCard;
    private int userId;
    private Button currSelected;


    public PayScreen(Stage stage, GuiClient app, Runnable onBack, String[] movie, int numTickets, double total) {
        this.stage   = stage;
        this.app     = app;
        this.onBack  = onBack;
        this.movie = movie;
        this.numTickets = numTickets;
        this.total = total;
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

        // Title
        Label title = new Label("Payment Methods");
        title.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                        "-fx-font-size: 26px;" +
                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

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

            // TODO CHANGE BACK TO ACTUAL UPDATES VERSION
            int bookingId = 0; // TEMP
            stage.setScene(new ConfirmScreen(stage, app, onBack, movie, numTickets, total, bookingId).getScene());
            // temp comment out so i dont make a million sales while doing the screen

//            int screeningId = Integer.parseInt(movie[5]);
//            Integer bookingId = app.book(screeningId, numTickets, total, selectedCard);
//
//            if (bookingId != null) {
//                stage.setScene(new ConfirmScreen(stage, app, onBack, movie, numTickets, total, bookingId).getScene());
//            } else {
//                System.out.println("Booking failed");
//            }
        });

        // Cards list
        VBox cardsBox = new VBox(8);
        cardsBox.setFillWidth(true);

        final Runnable[] loadCardsRef = new Runnable[1];

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
                    cardsBox.getChildren().add(cardRow(card, loadCardsRef, confirmBtn));
                }
            }
        };

        loadCardsRef[0].run();

//        Label movieName = new Label(movie[1]);
//        movieName.setStyle(
//                "-fx-font-family: 'Noto Serif Display';" +
//                        "-fx-font-size: 32px;" +
//                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
//        );

//        HBox details = getDetails();

//        VBox cardsBox = new VBox(8);
        List<String[]> cards = app.getPaymentMethods();

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

        VBox root = new VBox(navbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
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

    private String bodyTextStyle(int size) {
        return  "-fx-font-family: 'Questrial';" +
                "-fx-font-size: " + size + "px;" +
                "-fx-text-fill: " + LandingScreen.TEXT_MUTED + ";";
    }

    private HBox orderSummary(Button addBtn) {
        Label ticketsLbl = new Label("# Tickets");
        ticketsLbl.setStyle(bodyTextStyle(20));

        Label ticketCost = new Label(String.valueOf(numTickets));
        ticketCost.setStyle(bodyTextStyle(20));

        VBox ticketBox = new VBox(4, ticketsLbl, ticketCost);

        Label totalLbl = new Label("Total");
        totalLbl.setStyle(bodyTextStyle(20));

        Label totalCost = new Label("$" + total);
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