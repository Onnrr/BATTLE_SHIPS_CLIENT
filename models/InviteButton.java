package models;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

/**
 * Extends javafx button
 * Stores sender id sender name and root pane that is the parent of the button
 */
public class InviteButton extends Button {
    int id;
    String name;
    AnchorPane root;

    /**
     * Creates the button with given information
     * Root is null when button is created
     * 
     * @param id       id of the player that the invitation will be sent to
     * @param userName name of the player that the invitation will be sent to
     */
    public InviteButton(int id, String userName) {
        this.id = id;
        name = userName;
        root = null;
    }

    /**
     * Getter for id
     * 
     * @return id of the player that hte invitation will be sent to
     */
    public int getID() {
        return id;
    }

    /**
     * Getter for name
     * 
     * @return name of the player that hte invitation will be sent to
     */
    public String getName() {
        return name;
    }

    /**
     * Setter for root
     * 
     * @param pane anchor pane that is the parent of the button
     */
    public void setRoot(AnchorPane pane) {
        root = pane;
    }

    /**
     * Setter for root anchor pane that is the parent of the button
     */
    public AnchorPane getRoot() {
        return root;
    }
}
