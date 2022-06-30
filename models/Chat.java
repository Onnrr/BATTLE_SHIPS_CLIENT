package models;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class Chat extends AnchorPane {
    final String MESSAGE = "message";
    final int MESSAGE_TO_DISPLAY = 7;
    int numberOfMessages;
    VBox messageBox;
    TextField field;
    Button sendButton;
    Player p;
    ImageView sendImage;
    ArrayList<Notification> messages;

    public Chat(Player player, TextField textField, Button button) {
        p = player;
        numberOfMessages = 0;
        this.getStyleClass().add("chat");
        messageBox = new VBox();
        messageBox.getStyleClass().add("aa");

        messages = new ArrayList<Notification>();

        field = textField;
        field.setPromptText("Your message");
        sendButton = button;
        sendButton.getStyleClass().add("send");

        sendButton.setOnAction(e -> {
            send(e);
        });

        field.setOnAction(e -> {
            send(e);
        });

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
        AnchorPane.setBottomAnchor(messageBox, 60.0);

        // DUMMY MESSAGES
        for (int i = 0; i < MESSAGE_TO_DISPLAY; i++) {
            Notification not = new Notification("", "", "");
            not.setVisible(false);
            messages.add(not);
            messageBox.getChildren().add(not);
        }

        this.getChildren().addAll(messageBox, field, sendButton);
    }

    private void send(ActionEvent e) {
        if (field.getText().equals("")) {
            return;
        }
        SimpleDateFormat formatter = new SimpleDateFormat("HH.mm");
        Date date = new Date(System.currentTimeMillis());

        messageBox.getChildren().remove(messages.get(0));
        messages.remove(0);
        Notification newMessage = new Notification(p.getName(), formatter.format(date), field.getText());
        messages.add(newMessage);
        messageBox.getChildren().add(newMessage);

        p.sendMessage(MESSAGE + " " + field.getText());

        field.clear();
    }

    public void receiveMessage(String sender, String message) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                SimpleDateFormat formatter = new SimpleDateFormat("HH.mm");
                Date date = new Date(System.currentTimeMillis());

                messageBox.getChildren().remove(messages.get(0));
                messages.remove(0);
                Notification newMessage = new Notification(sender, formatter.format(date), message);
                messages.add(newMessage);
                messageBox.getChildren().add(newMessage);
            }
        });

    }
}
