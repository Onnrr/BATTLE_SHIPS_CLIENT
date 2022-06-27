package models;

import java.io.File;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class OnlinePlayer extends AnchorPane {
    Text userNameText;
    public String name;
    int status;

    Button joinButton;
    ImageView image;
    ImageView invite;
    ImageView join;

    public OnlinePlayer(String name, int status, Button inviteButton) {
        this.name = name;
        this.status = status;
        userNameText = new Text(name);

        image = new ImageView();
        invite = new ImageView();
        join = new ImageView();

        File file = new File("images/account.png");
        Image img = new Image(file.toURI().toString());
        image.setImage(img);

        file = new File("images/invite.png");
        img = new Image(file.toURI().toString());
        invite.setImage(img);
        invite.setFitHeight(20);
        invite.setFitWidth(20);

        file = new File("images/plus.png");
        img = new Image(file.toURI().toString());
        join.setImage(img);
        join.setFitHeight(20);
        join.setFitWidth(20);

        image.minHeight(100);
        image.minWidth(100);

        this.getStylesheets().add(getClass().getResource("/stylesheets/style.css").toExternalForm());
        this.getStyleClass().add("online");
        userNameText.getStyleClass().add("onlineUserName");

        this.setMaxHeight(150);
        this.setMaxWidth(150);
        this.setMinHeight(150);
        this.setMinWidth(150);

        inviteButton.setMinSize(30, 30);
        inviteButton.setMaxSize(30, 30);
        joinButton = new Button();
        joinButton.setMinSize(30, 30);
        joinButton.setMaxSize(30, 30);

        inviteButton.setGraphic(invite);
        joinButton.setGraphic(join);

        AnchorPane.setTopAnchor(userNameText, 0.0);
        AnchorPane.setLeftAnchor(userNameText, 5.0);
        AnchorPane.setBottomAnchor(image, 35.0);
        AnchorPane.setLeftAnchor(image, 30.0);

        AnchorPane.setBottomAnchor(inviteButton, 5.0);
        AnchorPane.setLeftAnchor(inviteButton, 5.0);

        AnchorPane.setBottomAnchor(joinButton, 5.0);
        AnchorPane.setRightAnchor(joinButton, 5.0);

        this.getChildren().add(userNameText);
        this.getChildren().add(joinButton);
        this.getChildren().add(inviteButton);
        this.getChildren().add(image);
    }

    public String getName() {
        return name;
    }

    public void setStatus(int stat) {
        status = stat;
    }
}
