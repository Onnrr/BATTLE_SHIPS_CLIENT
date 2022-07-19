package models;

import java.io.File;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class OnlinePlayer extends AnchorPane {
    Text userNameText;
    AnchorPane inGameLabel;
    public String name;
    int status;
    int id;
    InviteButton inviteButton;
    ImageView image;
    ImageView invite;
    ImageView join;

    public OnlinePlayer(String name, int ID, int status, InviteButton inviteButton) {
        this.inviteButton = inviteButton;
        id = ID;
        this.name = name;
        this.status = status;
        userNameText = new Text(name);

        Text inGame = new Text("in game");
        inGame.getStyleClass().add("gametext");

        inGameLabel = new AnchorPane();
        inGameLabel.setMaxSize(55, 20);
        inGameLabel.setMinSize(55, 20);
        inGameLabel.getStyleClass().add("ingame");
        AnchorPane.setBottomAnchor(inGame, 5.0);
        AnchorPane.setLeftAnchor(inGame, 5.0);
        inGameLabel.getChildren().add(inGame);

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

        image.minHeight(100);
        image.minWidth(100);

        this.getStylesheets().add(getClass().getResource("/stylesheets/player.css").toExternalForm());

        if (status == 0) {
            this.getStyleClass().add("online");
            inviteButton.setDisable(false);
        } else {
            this.getStyleClass().add("inGame");
            inviteButton.setDisable(true);
        }

        userNameText.getStyleClass().add("onlineUserName");

        this.setMaxHeight(150);
        this.setMaxWidth(150);
        this.setMinHeight(150);
        this.setMinWidth(150);

        inviteButton.setMinSize(30, 30);

        inviteButton.setGraphic(invite);
        inviteButton.setText("Invite");

        AnchorPane.setTopAnchor(userNameText, 0.0);
        AnchorPane.setLeftAnchor(userNameText, 5.0);
        AnchorPane.setBottomAnchor(image, 35.0);
        AnchorPane.setLeftAnchor(image, 30.0);

        AnchorPane.setBottomAnchor(inviteButton, 5.0);
        AnchorPane.setLeftAnchor(inviteButton, 5.0);

        AnchorPane.setTopAnchor(inGameLabel, -5.0);
        AnchorPane.setRightAnchor(inGameLabel, -5.0);

        inGameLabel.setVisible(false);
        inGameLabel.setDisable(true);
        this.getChildren().add(userNameText);
        this.getChildren().add(inviteButton);
        this.getChildren().add(image);
        this.getChildren().add(inGameLabel);
    }

    public String getName() {
        return name;
    }

    public void setStatus(int stat) {
        status = stat;
    }

    public int getID() {
        return id;
    }

    public InviteButton getInviteButton() {
        return inviteButton;
    }

    public void inGame() {
        status = 1;
        inviteButton.setDisable(true);
        this.getStyleClass().remove("online");
        this.getStyleClass().add("inGame");
        inGameLabel.setVisible(true);
        inGameLabel.setDisable(false);
    }

    public void leftGame() {
        status = 0;
        inviteButton.setDisable(false);
        this.getStyleClass().remove("inGame");
        this.getStyleClass().add("online");
        inGameLabel.setVisible(false);
        inGameLabel.setDisable(true);
    }
}
