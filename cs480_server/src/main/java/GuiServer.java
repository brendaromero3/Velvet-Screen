
import java.util.HashMap;
import java.util.function.Consumer;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.control.Label;

public class GuiServer extends Application{

    Server serverConnection;

    ListView<String> listItems;
    ListView<String> listUsers;

    HBox lists;
    VBox activeClients, activityLogs;


    public static void main(String[] args) {
        launch(args);
    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        listItems = new ListView<String>();
        listUsers = new ListView<String>();

        serverConnection = new Server(data->{
            Platform.runLater(()->{
                switch (data.type){
                    case TEXT:
                        listItems.getItems().add(data.username+" to "+data.recipient+": "+data.message);
                        break;
                    case NEWUSER:
                        listUsers.getItems().add(String.valueOf(data.username));
                        listItems.getItems().add(data.username + " has joined!");
                        break;
                    case DISCONNECT:
                        listUsers.getItems().remove(String.valueOf(data.username));
                        listItems.getItems().add(data.username + " has disconnected!");
                }
            });
        }, listItems);

        Label active = new Label("Active Users");
        Label logs = new Label("Activity Log");

        activeClients = new VBox(active, listUsers);
        activityLogs = new VBox(logs, listItems);
        lists = new HBox(activeClients, activityLogs);


        BorderPane pane = new BorderPane();
        pane.setPadding(new Insets(90));
        pane.setStyle("-fx-background-color: coral");

        pane.setCenter(lists);
        pane.setStyle("-fx-font-family: 'serif'");
        ;


        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                Platform.exit();
                System.exit(0);
            }
        });

        primaryStage.setScene(new Scene(pane, 700, 550));
        primaryStage.setTitle("Server");
        primaryStage.show();

    }
}
