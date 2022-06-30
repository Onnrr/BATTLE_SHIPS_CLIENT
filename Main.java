
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.Initialise;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("views/login.fxml"));
        loader.load();
        Parent p = loader.getRoot();
        Scene scene = new Scene(p);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Battle Ships");
        primaryStage.setFullScreen(false);
        primaryStage.setHeight(700);
        primaryStage.setWidth(1200);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(600);
        primaryStage.setMaxHeight(1000);
        primaryStage.setMaxWidth(1535);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
