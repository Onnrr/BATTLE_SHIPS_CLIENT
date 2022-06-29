package models;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

public class Chat extends AnchorPane {
    final int MESSAGE_TO_DISPLAY = 8;
    VBox messageBox;
    TextField field;
    Button sendButton;
    Player p;
    int numberOfMessages;
    ImageView sendImage;

    public Chat(Player player) {
        numberOfMessages = 0;
        this.getStyleClass().add("chat");
        messageBox = new VBox();
        messageBox.getStyleClass().add("aa");

        field = new TextField();
        field.setPromptText("Your message");
        sendButton = new Button();
        sendButton.getStyleClass().add("send");

        sendImage = new ImageView();

        File file = new File("images/send.png");
        Image img = new Image(file.toURI().toString());
        sendImage.setImage(img);
        sendImage.getStyleClass().add("icon");
        sendImage.setFitHeight(30);
        sendImage.setFitWidth(30);

        sendButton.setGraphic(sendImage);

        AnchorPane.setRightAnchor(sendButton, 10.0);
        AnchorPane.setBottomAnchor(sendButton, 10.0);

        AnchorPane.setBottomAnchor(field, 10.0);
        AnchorPane.setLeftAnchor(field, 10.0);
        AnchorPane.setRightAnchor(field, 70.0);

        AnchorPane.setTopAnchor(messageBox, 10.0);
        AnchorPane.setRightAnchor(messageBox, 10.0);
        AnchorPane.setLeftAnchor(messageBox, 10.0);
        AnchorPane.setBottomAnchor(messageBox, 100.0);

        Notification not = new Notification("asdfdsfsaf", "sdjkfhasfjadifasdfs", 0);

        messageBox.getChildren().add(not);

        this.getChildren().addAll(messageBox, field, sendButton);
    }
}
