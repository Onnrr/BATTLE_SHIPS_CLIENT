package models;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

public class InviteButton extends Button {
    int id;
    String name;
    AnchorPane root;

    public InviteButton(int id, String userName) {
        this.id = id;
        name = userName;
        root = null;
    }

    public int getID() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setRoot(AnchorPane pane) {
        root = pane;
    }

    public AnchorPane getRoot() {
        return root;
    }
}
