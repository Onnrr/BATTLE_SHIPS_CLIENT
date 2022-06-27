
package models;

import java.io.IOException;
import java.net.URL;
import javafx.scene.Node;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppManager {
    public static void changeScene(URL fxmlfile, ActionEvent e, Player player) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(fxmlfile);
        loader.load();
        Initialise c2 = loader.getController();
        c2.initialise(player);
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(p, ((Node) e.getSource()).getScene().getWidth(),
                ((Node) e.getSource()).getScene().getHeight());

        ((Stage) ((Node) e.getSource()).getScene().getWindow()).setOnCloseRequest(event -> {
            player.sendMessage("disconnect");
        });

        stage.setWidth(stage.getWidth() + 0.0001);
        stage.setScene(scene);
        stage.show();
    }

}
