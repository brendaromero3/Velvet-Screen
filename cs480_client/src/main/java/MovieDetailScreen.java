import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
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

public class MovieDetailScreen {

    private Stage stage;
    private GuiClient app;
    private Runnable onBack;
    private Runnable onLogout;
    private String[] movie;
    private Runnable onAccount;
    private Runnable onPayments;
    private Runnable onBrowse;

    public MovieDetailScreen(Stage stage, GuiClient app, Runnable onBack, Runnable onBrowse, Runnable onLogout, Runnable onAccount, Runnable onPayments, String[] movie) {
        this.stage   = stage;
        this.app     = app;
        this.onBack  = onBack;
        this.onBrowse = onBrowse;
        this.onLogout = onLogout;
        this.onAccount = onAccount;
        this.onPayments = onPayments;
        this.movie  = movie;
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

        browseBtn.setOnAction(e -> onBrowse.run());
        accountBtn.setOnAction(e -> { if (onAccount != null) onAccount.run(); });
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

        List<String[]> actors = app.getActors(movie[0]);
        List<String[]> directors = app.getDirectors(movie[0]);
        List<String[]> writers = app.getWriters(movie[0]);
        List<String[]> producers = app.getProducers(movie[0]);

//        String movieId = movie[0];
        Label movieName = new Label(movie[1]);
        movieName.setStyle(
                "-fx-font-family: 'Noto Serif Display';" +
                        "-fx-font-size: 32px;" +
                        "-fx-text-fill: " + LandingScreen.TEXT_DARK + ";"
        );

        FlowPane people = getPeople(actors, directors, writers, producers);

        FlowPane details = getDetails();

        GridPane table = screeningTable();

        ScrollPane scroll = new ScrollPane(table);
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

        VBox content = new VBox(30, movieName, people, details, scroll);
        content.setPadding(new Insets(30, 40, 30, 40));
        VBox.setVgrow(scroll, Priority.ALWAYS);

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

    private FlowPane getPeople(List<String[]> actors, List<String[]> directors,
                           List<String[]> writers, List<String[]> producers) {

        // Gets all Actors
        Label cast = new Label("Cast");
        cast.setStyle(bodyTextStyle(21));

        VBox act = new VBox(4, cast);
        for (String[] actor : actors) {
            String name = actor[0];
            if (actor.length > 1) {
                name += " as " + actor[1];
            } else { name += "";}

            Label ppl = new Label(name);
            ppl.setStyle(bodyTextStyle(15));
            act.getChildren().add(ppl);
        }

        // Gets all Directors
        Label dir = new Label("Directors");
        dir.setStyle(bodyTextStyle(21));

        VBox d = new VBox(4, dir);
        for (String[] director : directors) {
            Label ppl = new Label(director[0]);
            ppl.setStyle(bodyTextStyle(15));
            d.getChildren().add(ppl);

        }

        // Gets all Writers
        Label write = new Label("Writers");
        write.setStyle(bodyTextStyle(21));

        VBox w = new VBox(4, write);
        for (String[] writer : writers) {
            Label ppl = new Label(writer[0]);
            ppl.setStyle(bodyTextStyle(15));
            w.getChildren().add(ppl);

        }

        // Gets all Producers
        Label pro = new Label("Producers");
        pro.setStyle(bodyTextStyle(21));

        VBox p = new VBox(4, pro);
        for (String[] producer : producers) {
            Label ppl = new Label(producer[0]);
            ppl.setStyle(bodyTextStyle(15));
            p.getChildren().add(ppl);
        }

        FlowPane panePpl = new FlowPane();
        panePpl.setVgap(40);
        panePpl.setHgap(80);
        panePpl.setPadding(new Insets(10));
        panePpl.setPrefWrapLength(300);
        panePpl.getChildren().addAll(act, d, w, p);

        return panePpl;
    }

    private FlowPane getDetails() {
        // Movie language
        Label language = new Label("Language");
        language.setStyle(bodyTextStyle(21));

        Label lang = new Label(movie[2]);
        lang.setStyle(bodyTextStyle(15));

        VBox l = new VBox(16, language, lang);

        // Movie length
        Label length = new Label("Movie Length");
        length.setStyle(bodyTextStyle(21));

        int total = Integer.parseInt(movie[12]);
        int hours = total / 60;
        int minutes = total % 60;

        Label len = new Label(hours + "h " + minutes + "m");
        len.setStyle(bodyTextStyle(15));

        VBox s = new VBox(16, length, len);

        // Movie Release Date
        Label releaseDate = new Label("Release Date");
        releaseDate.setStyle(bodyTextStyle(21));

        Label release = new Label(movie[3]);
        release.setStyle(bodyTextStyle(15));

        VBox r = new VBox(16, releaseDate, release);

        FlowPane paneDetails = new FlowPane();
        paneDetails.setVgap(40);
        paneDetails.setHgap(80);
        paneDetails.setPadding(new Insets(10));
        paneDetails.setPrefWrapLength(300);
        paneDetails.getChildren().addAll( l, s, r);

        return paneDetails;
    }

    private GridPane screeningTable() {
        GridPane table = new GridPane();
        table.setHgap(60);
        table.setVgap(30);
        table.setPadding(new Insets(10));

        table.setStyle(
                "-fx-border-color: " + LandingScreen.BORDER_COLOR + ";" +
                        "-fx-border-width: 1;" +
                        "-fx-background-color: white;"
        );

        headers(table, "Date and Time", 0);
        headers(table, "Theater", 1);
        headers(table, "Features", 2);
        headers(table, "Available Seats", 3);
        headers(table, "Ticket Price", 4);
        headers(table, "Book", 5);

        List<String[]> screening = app.getMovies(true, movie[1], null, null, null, null, null);

        // adding each screening to table
        int row = 1;
        for (String[] screen : screening) {
            String DandT = screen[6];
            String theater = screen[7];
            String seats = screen[11];
            int price = 15;

            boolean is3D = false;
            boolean isFancy = false;
            boolean isMajor = false;
            if (screen[9].equals("t")){
                is3D = true;
            }
            if (screen[10].equals("t")){
                isFancy = true;
            }
            if (screen[4].equals("t")) {
                isMajor = true;
            }

            Map<String, Integer> surcharges = new HashMap<>();
            surcharges.put("Base Price", 15);

            // Gets the different features of the theater and adjusts the price
            String features = "";
            if (is3D && isFancy) {
                features = "3D and Fancy Sound";
                price += 8;
                surcharges.put("3D", 5);
                surcharges.put("Fancy Sound", 3);
            } else if (is3D) {
                features = "3D";
                price += 5;
                surcharges.put("3D", 5);
            } else if (isFancy) {
                features = "Fancy Sound";
                price += 3;
                surcharges.put("Fancy Sound", 3);
            } else {
                features = "None";
            }

            // checks if movie is from a major studio
            if (isMajor) {
                price += 3;
                surcharges.put("Major Studio", 3);
            }

            // checks if a discounts can be applied based on the release date and current date
            price = discount(screen, price, surcharges);

            String p = "$" + price;

            contents(table, DandT, 0, row);
            contents(table, theater, 1, row);
            contents(table, features, 2, row);
            contents(table, seats, 3, row);
            contents(table, p, 4, row);

            // creates button to book that screening
            Button book = new Button("Book");
            book.setStyle(
                    "-fx-background-color: " + LandingScreen.PRIMARY_COLOR + ";" +
                            "-fx-text-fill: white;" +
                            "-fx-font-family: 'Questrial';" +
                            "-fx-font-size: 13px;" +
                            "-fx-padding: 10 10 10 10;" +
                            "-fx-border-radius: 4;" +
                            "-fx-background-radius: 4;" +
                            "-fx-cursor: hand;"
            );
            int bookPrice = price;
            book.setOnAction(e -> {
                stage.setScene(new BookingScreen(stage, app, onBrowse, onBack, screen, DandT, theater, bookPrice, surcharges).getScene());
            });

            table.add(book, 5, row); // adds the button the GridPane

            if (Integer.parseInt(screen[11]) <= 0) {
                book.setText("Sold Out");
                book.setDisable(true);
            }

            row++;
        }

        return table;
    }

    private void headers(GridPane table, String text, int col) {
        Label header = new Label(text);
        header.setStyle(bodyTextStyle(15));

        header.setWrapText(true);
        table.setHalignment(header, HPos.CENTER);
        table.setValignment(header, VPos.CENTER);

        table.add(header, col, 0);
    }

    private void contents(GridPane table, String text, int col, int row) {
        Label content = new Label(text);
        content.setStyle(bodyTextStyle(15));

        content.setWrapText(true);
        table.setHalignment(content, HPos.CENTER);
        table.setValignment(content, VPos.CENTER);

        table.add(content, col, row);
    }

    private int discount(String[] screen, int price, Map<String, Integer> surcharges) {
        // checking for discounts based on date

        // From API: Obtains an instance of LocalDate from a text string
        LocalDate date = LocalDate.parse(screen[3]);

        // calculates the date of two months and two years ago from the current date
        LocalDate minus2Months = LocalDate.now().minusMonths(2);
        LocalDate minus2Years = LocalDate.now().minusYears(2);

        // checks if the movie's release date is before the 2 months and 2 years date
        Boolean months = date.isBefore(minus2Months);
        Boolean years = date.isBefore(minus2Years);

        if (years) {
//            price *= 0.6; // 60% of original price/40% off
            int disc = (int) (price * 0.4);
            price -= disc;
            surcharges.put("Movie over 2 years old", -disc);
        }
        else if (months) {
//            price *= 0.8; // 80% of original price/20% off
            int disc = (int) (price * 0.2);
            price -= disc;
            surcharges.put("Movie over 2 months old", -disc);
        }

        return price;
    }
}