package models;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;

public class OnlinePlayer extends AnchorPane {
    Text userNameText;
    public String name;
    int status;

    public OnlinePlayer(String name, int status) {
        this.name = name;
        this.status = status;
        userNameText = new Text(name);

        this.getStylesheets().add(getClass().getResource("/stylesheets/style.css").toExternalForm());
        this.getStyleClass().add("online");
        userNameText.getStyleClass().add("onlineUserName");

        this.setMaxHeight(150);
        this.setMaxWidth(150);
        this.setMinHeight(150);
        this.setMinWidth(150);

        AnchorPane.setTopAnchor(userNameText, 5.0);
        AnchorPane.setLeftAnchor(userNameText, 5.0);

        this.getChildren().add(userNameText);

    }

    public String getName() {
        return name;
    }
}
