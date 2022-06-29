package models;

import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Settings extends AnchorPane {
    final String DISCONNECT = "disconnect";
    final String DELETE_ACCOUNT = "delete";
    final String GAME_EXPLANATION = "Lorem ipsum dolor sit amet, consectetur\n adipiscing elit. Nunc sed condimentum nisl,\n eget varius ante. Nullam non diam vel nisi\n finibus sagittis. Fusce ut risus et mauris\n lacinia dignissim in eu leo. Donec\n varius ligula lobortis ornare iaculis.\n Aliquam erat volutpat. Suspendisse\n egestas diam turpis, vel scelerisque neque\n suscipit eget. Ut pellentesque enim ut nibh\n rutrum, quis volutpat ex finibus. Etiam\n tortor tellus, luctus in cursus et,\n luctus non eros.";
    ImageView profile;
    Text userName;
    Text mail;
    Text score;
    Button deleteAccount;
    Button logout;
    Text accountTitle;
    Text howTo;
    Text explanation;
    Player p;

    public Settings(Player player) {
        p = player;
        userName = new Text("User Name : " + p.getName());
        score = new Text("Score : " + p.getScore());
        explanation = new Text(GAME_EXPLANATION);
        deleteAccount = new Button("Delete Account");
        logout = new Button("Logout");
        accountTitle = new Text("Account");
        accountTitle.getStyleClass().add("settingsTitle");
        mail = new Text("Mail : " + p.getMail());
        mail.getStyleClass().add("settingsText");

        logout.getStyleClass().add("logoutButton");

        logout.setOnMouseClicked(e -> {
            logout(e);
        });

        deleteAccount.setOnMouseClicked(e -> {
            deleteAccount(e);
        });

        deleteAccount.getStyleClass().add("deleteButton");
        userName.getStyleClass().add("settingsText");

        score.getStyleClass().add("settingsText");

        howTo = new Text("How To Play?");
        howTo.getStyleClass().add("settingsTitle");

        explanation.getStyleClass().add("settingsText");
        explanation.maxWidth(10);
        AnchorPane.setTopAnchor(userName, 70.0);
        AnchorPane.setLeftAnchor(userName, 20.0);
        AnchorPane.setTopAnchor(score, 170.0);
        AnchorPane.setLeftAnchor(score, 20.0);

        AnchorPane.setBottomAnchor(logout, 30.0);
        AnchorPane.setLeftAnchor(logout, 20.0);

        AnchorPane.setBottomAnchor(deleteAccount, 30.0);
        AnchorPane.setLeftAnchor(deleteAccount, 120.0);

        AnchorPane.setRightAnchor(howTo, 200.0);
        AnchorPane.setTopAnchor(howTo, 20.0);

        AnchorPane.setTopAnchor(accountTitle, 20.0);
        AnchorPane.setLeftAnchor(accountTitle, 20.0);

        AnchorPane.setLeftAnchor(explanation, 400.0);
        AnchorPane.setTopAnchor(explanation, 70.0);

        AnchorPane.setTopAnchor(mail, 120.0);
        AnchorPane.setLeftAnchor(mail, 20.0);

        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().addAll(userName, score, logout, deleteAccount, howTo, explanation, accountTitle, mail);
        this.getStyleClass().add("settings");

    }

    public void logout(MouseEvent e) {
        p.sendMessage(DISCONNECT);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login.fxml"));
        try {
            loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(p, ((Node) e.getSource()).getScene().getWidth(),
                ((Node) e.getSource()).getScene().getHeight());

        stage.setHeight(stage.getHeight() + 0.0001);

        stage.setScene(scene);
        stage.show();
    }

    public void deleteAccount(MouseEvent e) {
        p.sendMessage(DELETE_ACCOUNT);
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/login.fxml"));
        try {
            loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(p, ((Node) e.getSource()).getScene().getWidth(),
                ((Node) e.getSource()).getScene().getHeight());

        stage.setHeight(stage.getHeight() + 0.0001);

        stage.setScene(scene);
        stage.show();
    }

}
