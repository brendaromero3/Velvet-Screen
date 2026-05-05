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
import javafx.scene.layout.FlowPane;
import java.lang.Math;

public class BookingScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onTitle;
    private String[] movie;
    private String screen_time;
    private String theater_id;
    private int price;
    private Map<String, Integer> surcharges;
    private double ticketTotal = 0;
    private boolean useFreeTickets = false;
    private int freeTicketsUsed = 0;
    private double origTotal;
    private Label totalValueLbl;

    public BookingScreen(Stage stage, GuiClient app, Runnable onBack, Runnable onTitle, String[] movie, String screen_time, String theater_id, int price, Map<String, Integer> surcharges) {
        this.stage   = stage;
        this.app     = app;
        this.onBack  = onBack;
        this.onTitle  = onTitle;
        this.movie  = movie;
        this.screen_time = screen_time;
        this.theater_id = theater_id;
        this.price = price;
        this.surcharges = surcharges;
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

        Label movieName = new Label(movie[1]);
        movieName.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                        "-fx-font-size: 32px;" +
                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        FlowPane details = getDetails();

        Spinner<Integer> ticketSpinner = new Spinner<>(1, Integer.parseInt(movie[11]), 1);
        ticketSpinner.setEditable(true);
        Label ticketLabel = new Label("Number of Tickets");
        ticketLabel.setStyle(bodyTextStyle(24));

        Label freeLbl = new Label("Use it? ");
        freeLbl.setStyle(bodyTextStyle(18));

        Label howManyLbl = new Label("You have " + app.getFreeTickets() + " free ticket(s) ");
        howManyLbl.setStyle(bodyTextStyle(18));

        CheckBox freeCheckBox = new CheckBox();

        freeCheckBox.setOnAction(e -> {
            useFreeTickets = freeCheckBox.isSelected();

            int numTickets = ticketSpinner.getValue();
            int freeCount = app.getFreeTickets();
            int usableFree = Math.min(freeCount, numTickets);

            if (useFreeTickets) {
                freeTicketsUsed = usableFree;
            } else {
                freeTicketsUsed = 0;
            }

            double ticketPrice = origTotal / numTickets;
            ticketTotal = (numTickets - freeTicketsUsed) * ticketPrice;

            totalValueLbl.setText("$" + ticketTotal);
        });

        boolean hasMem = app.hasMembership();

        HBox temp = new HBox();
        VBox freeBox = new VBox();

        if (hasMem) {
             temp.getChildren().addAll(freeLbl, freeCheckBox);
             freeBox.getChildren().addAll(howManyLbl, temp);
        }

        VBox ticketBox = new VBox(10, ticketLabel, ticketSpinner, freeBox);

        VBox prices = priceBreakdown(ticketSpinner.getValue());

        ticketSpinner.valueProperty().addListener((obs, oldVal, newVal) -> {
            prices.getChildren().setAll(priceBreakdown(newVal));

            int numTickets = newVal;

            if (useFreeTickets) {
                int freeCount = app.getFreeTickets();
                int usableFree = Math.min(freeCount, numTickets);
                freeTicketsUsed = usableFree;

                double ticketPrice = origTotal / numTickets;
                ticketTotal = (numTickets - freeTicketsUsed) * ticketPrice;

                totalValueLbl.setText("$" + ticketTotal);
            }
        });

        Button payBtn = new Button("Purchase");
        payBtn.setStyle(
                "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Questrial';" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 10 10 10;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );

        HBox priceInfo = new HBox(100, ticketBox, prices, payBtn);
        priceInfo.setAlignment(Pos.CENTER);

        payBtn.setOnAction(e -> {
            stage.setScene(new PayScreen(stage, app, onBack, movie, ticketSpinner.getValue(), ticketTotal, useFreeTickets, freeTicketsUsed).getScene());
        });

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

        VBox content = new VBox(30, movieName, details, priceInfo, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);

        VBox root = new VBox(navbar, content);
        VBox.setVgrow(content, Priority.ALWAYS);
        root.setStyle("-fx-background-color: " + LandingScreen.BG_COLOR + ";");
        root.setAlignment(Pos.CENTER);

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

    private FlowPane getDetails() {
        //show the movie title, screening time, theater, and a price breakdown showing the base price,
        // any surcharges, and the total

        // Screening Time
        Label dateTime = new Label("Screening Time");
        dateTime.setStyle(bodyTextStyle(21));

        Label dt = new Label(screen_time);
        dt.setStyle(bodyTextStyle(18));

        VBox s = new VBox(16, dateTime, dt);

        // Movie length
        Label length = new Label("Movie Length");
        length.setStyle(bodyTextStyle(21));

        int total = Integer.parseInt(movie[12]);
        int hours = total / 60;
        int minutes = total % 60;

        Label len = new Label(hours + "h " + minutes + "m");
        len.setStyle(bodyTextStyle(18));

        VBox l = new VBox(16, length, len);

        // Theater Number
        Label theater_num = new Label("Theater Number");
        theater_num.setStyle(bodyTextStyle(21));

        Label t = new Label(theater_id);
        t.setStyle(bodyTextStyle(18));

        VBox n = new VBox(16, theater_num, t);

        FlowPane paneDetails = new FlowPane();
        paneDetails.setVgap(40);
        paneDetails.setHgap(80);
        paneDetails.setPadding(new Insets(10));
        paneDetails.setPrefWrapLength(300);
        paneDetails.getChildren().addAll(s, l, n);

        return paneDetails;
    }

    private VBox priceBreakdown(int numTickets) {
        VBox p = new VBox(10);

        Label label = new Label("Price");
        label.setStyle(bodyTextStyle(24));

        Separator topLine = new Separator();

        VBox surcharge = new VBox(6);

        int runningTotal = 0;

        // loops through map and lists all surcharges and discounts
        for (Map.Entry<String, Integer> entry : surcharges.entrySet()) {
            String charge = entry.getKey();
            int price = entry.getValue();

            runningTotal += price;

            HBox row = new HBox(10);
            Label chargeName = new Label(charge);
            chargeName.setStyle(bodyTextStyle(20));

            Label chargeCost = new Label("$" + (price * numTickets));
            chargeCost.setStyle(bodyTextStyle(20));

            Region spacer = new Region();
            HBox.setHgrow(spacer, Priority.ALWAYS);

            row.getChildren().addAll(chargeName, spacer, chargeCost);
            row.setPadding(new Insets(0, 0, 0, 20));
            surcharge.getChildren().add(row);
        }
        Separator bottomLine = new Separator();

        HBox totalCost = new HBox(10);

        Label totalLabel = new Label("Total :");
        totalLabel.setStyle(bodyTextStyle(24));

        double cost = runningTotal * numTickets;
        ticketTotal = cost;
        origTotal = cost;

        totalValueLbl = new Label("$" + cost);
        totalValueLbl.setStyle(bodyTextStyle(20));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        totalCost.getChildren().addAll(totalLabel, spacer, totalValueLbl);

        p.getChildren().addAll(label, topLine, surcharge, bottomLine, totalCost);

        return new VBox(p);
    }

}