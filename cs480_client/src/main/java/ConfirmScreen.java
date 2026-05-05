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

public class ConfirmScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onTitle;
    private String[] movie;
    private int numTickets;
    private double total;
    private int bookingId;


    public ConfirmScreen(Stage stage, GuiClient app, Runnable onBack, String[] movie, int numTickets, double total, int bookingId) {
        this.stage = stage;
        this.app = app;
        this.onBack = onBack;
//        this.onTitle = onTitle;
        this.movie = movie;
        this.numTickets = numTickets;
        this.total = total;
        this.bookingId = bookingId;
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

//        Button backBtn   = navButton("Back");
//        backBtn.setOnAction(e -> onBack.run());

        Region navSpacer = new Region();
        HBox.setHgrow(navSpacer, Priority.ALWAYS);

        HBox navbar = new HBox(12, wordmark, navSpacer);
        navbar.setAlignment(Pos.CENTER_LEFT);
        navbar.setStyle(
                "-fx-padding: 16 40 16 40;" +
                        "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
                        "-fx-border-width: 0 0 1 0;" +
                        "-fx-background-color: " + LandingScreen.BG_COLOR + ";"
        );

        String movie_title = movie[1];
        Label orderConfirmation = new Label("Order Confirmation for " + movie_title + ":");
        orderConfirmation.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                        "-fx-font-size: 32px;" +
                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        VBox details = confirmation();

        Button homeBtn = navButton("Home");
        homeBtn.setStyle(
                "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                        "-fx-text-fill: white;" +
                        "-fx-font-family: 'Questrial';" +
                        "-fx-font-size: 20px;" +
                        "-fx-padding: 10 10 10 10;" +
                        "-fx-border-radius: 4;" +
                        "-fx-background-radius: 4;" +
                        "-fx-cursor: hand;"
        );
        homeBtn.setOnAction(e -> onBack.run());

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

        VBox content = new VBox(30, orderConfirmation, details, homeBtn, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);
        content.setAlignment(Pos.CENTER);

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

    private VBox confirmation() {
        // Booking Reference
        String booking_ref = String.valueOf(bookingId);
        VBox b = labels("Booking Reference Number", booking_ref);

        // Number of Tickets
        String numT = String.valueOf(numTickets);
        VBox num = labels("Number of Tickets", numT);

        // Screening Time
        String screen_time = movie[6];
        VBox s = labels("Screening Time", screen_time);

        // Theater Number
        String theater_id = movie[7];
        VBox n = labels("Theater Number", theater_id);

        // Total Paid
        String totalPaid = "$" + String.valueOf(total);
        VBox tot = labels("Total Paid", totalPaid);

        VBox box = new VBox(25, b, num, s, n, tot);
        box.setAlignment(Pos.CENTER);

        return box;
    }

    private VBox labels(String title, String value) {
        Label lbl1 = new Label(title);
        lbl1.setStyle(bodyTextStyle(21));

        Label lbl2 = new Label(value);
        lbl2.setStyle(bodyTextStyle(18));

        VBox box = new VBox(16, lbl1, lbl2);
        box.setAlignment(Pos.CENTER);

        return box;
    }
}