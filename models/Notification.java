package models;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class Notification extends AnchorPane {
    Text header;
    public Text message;
    Text rank;
    InviteButton acceptButton;
    InviteButton declineButton;

    int id;

    public Notification(String headerString, String messageString, InviteButton accept, InviteButton decline, int ID) {
        id = ID;
        this.setMaxHeight(100);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(100);
        this.getStylesheets().add(getClass().getResource("/stylesheets/lobby.css").toExternalForm());
        this.getStyleClass().add("notification");

        header = new Text(headerString);
        header.getStyleClass().add("onlineUserName");
        message = new Text(messageString);
        acceptButton = accept;
        declineButton = decline;

        ImageView acceptIcon = new ImageView();
        ImageView declineIcon = new ImageView();

        File file = new File("images/approve.png");
        Image img = new Image(file.toURI().toString());
        acceptIcon.setImage(img);
        acceptIcon.setFitHeight(20);
        acceptIcon.setFitWidth(20);

        file = new File("images/decline.png");
        img = new Image(file.toURI().toString());
        declineIcon.setImage(img);
        declineIcon.setFitHeight(20);
        declineIcon.setFitWidth(20);

        acceptButton.setGraphic(acceptIcon);
        declineButton.setGraphic(declineIcon);

        AnchorPane.setTopAnchor(header, 5.0);
        AnchorPane.setLeftAnchor(header, 10.0);

        AnchorPane.setTopAnchor(message, 35.0);
        AnchorPane.setLeftAnchor(message, 10.0);

        AnchorPane.setTopAnchor(acceptButton, 10.0);
        AnchorPane.setRightAnchor(acceptButton, 5.0);
        AnchorPane.setBottomAnchor(declineButton, 10.0);
        AnchorPane.setRightAnchor(declineButton, 5.0);

        this.getChildren().addAll(header, message, acceptButton, declineButton);
    }

    public Notification(String headerString, String messageString, int ID) {
        id = ID;
        this.setMaxHeight(100);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(100);
        this.getStylesheets().add(getClass().getResource("/stylesheets/lobby.css").toExternalForm());
        this.getStyleClass().add("notification");

        header = new Text(headerString);
        header.getStyleClass().add("onlineUserName");
        message = new Text(messageString);

        AnchorPane.setTopAnchor(header, 5.0);
        AnchorPane.setLeftAnchor(header, 10.0);

        AnchorPane.setTopAnchor(message, 35.0);
        AnchorPane.setLeftAnchor(message, 10.0);

        this.getChildren().addAll(header, message);
    }

    public Notification(int rank, String headerString, String messageString) {
        this.setMaxHeight(Double.MAX_VALUE);
        this.setMaxWidth(Double.MAX_VALUE);
        this.setMinHeight(95);
        this.getStylesheets().add(getClass().getResource("/stylesheets/lobby.css").toExternalForm());
        this.getStyleClass().add("notification");

        header = new Text(headerString);
        header.getStyleClass().add("onlineUserName");
        message = new Text(messageString);
        this.rank = new Text(String.valueOf(rank));

        AnchorPane.setTopAnchor(header, 5.0);
        AnchorPane.setLeftAnchor(header, 20.0);

        AnchorPane.setTopAnchor(message, 35.0);
        AnchorPane.setLeftAnchor(message, 20.0);

        AnchorPane.setTopAnchor(this.rank, 10.0);
        AnchorPane.setLeftAnchor(this.rank, 10.0);

        this.getChildren().addAll(header, message, this.rank);
    }

    public int getID() {
        return id;
    }
}
