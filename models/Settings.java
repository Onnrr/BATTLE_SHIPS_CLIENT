package models;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class Settings extends AnchorPane {
    final String DISCONNECT = "disconnect";
    final String DELETE_ACCOUNT = "delete";
    final String CHANGE_PASSWORD = "change_password";
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

    PasswordField exPassword;
    PasswordField newPassword;
    PasswordField repeatPassword;
    Button changePasswordButton;

    public Settings(Player player) {
        p = player;
        this.getStylesheets().add(getClass().getResource("/stylesheets/settings.css").toExternalForm());
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

        changePasswordButton = new Button("Change Password");
        exPassword = new PasswordField();
        newPassword = new PasswordField();
        repeatPassword = new PasswordField();
        exPassword.setPromptText("Current Password");
        newPassword.setPromptText("New Password");
        repeatPassword.setPromptText("Repeat Password");

        changePasswordButton.setOnAction(e -> {
            changePassword(e);
        });

        logout.setOnMouseClicked(e -> {
            logout(e);
        });

        deleteAccount.setOnMouseClicked(e -> {
            deleteAccount(e);
        });

        newPassword.setOnAction(e -> {
            changePassword(e);
        });

        changePasswordButton.getStyleClass().add("changePasswordButton");
        deleteAccount.getStyleClass().add("deleteButton");
        userName.getStyleClass().add("settingsText");

        score.getStyleClass().add("settingsText");

        howTo = new Text("How To Play?");
        howTo.getStyleClass().add("settingsTitle");

        explanation.getStyleClass().add("settingsText");
        explanation.maxWidth(10);

        exPassword.setMinWidth(300);
        newPassword.setMinWidth(300);
        repeatPassword.setMinWidth(300);

        changePasswordButton.setMinWidth(300);
        changePasswordButton.setMaxHeight(10);

        AnchorPane.setTopAnchor(userName, 70.0);
        AnchorPane.setLeftAnchor(userName, 20.0);
        AnchorPane.setTopAnchor(score, 150.0);
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

        AnchorPane.setTopAnchor(mail, 110.0);
        AnchorPane.setLeftAnchor(mail, 20.0);

        AnchorPane.setBottomAnchor(changePasswordButton, 110.0);
        AnchorPane.setLeftAnchor(changePasswordButton, 20.0);

        AnchorPane.setBottomAnchor(newPassword, 220.0);
        AnchorPane.setLeftAnchor(newPassword, 20.0);

        AnchorPane.setBottomAnchor(exPassword, 280.0);
        AnchorPane.setLeftAnchor(exPassword, 20.0);

        AnchorPane.setBottomAnchor(repeatPassword, 160.0);
        AnchorPane.setLeftAnchor(repeatPassword, 20.0);

        this.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
        this.getChildren().addAll(userName, score, logout, deleteAccount, howTo, explanation, accountTitle, mail,
                changePasswordButton, newPassword, exPassword, repeatPassword);
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

    public void changePassword(ActionEvent e) {
        if (exPassword.getText().equals("") || newPassword.getText().equals("")) {
            return;
        }
        if (newPassword.getText().length() < 4) {
            System.out.println("Password too short");
            return;
            // TODO
        }
        if (newPassword.getText().length() > 16) {
            System.out.println("Password too long");
            return;
            // TODO
        }
        if (!newPassword.getText().equals(repeatPassword.getText())) {
            System.out.println("Passwords don't match");
            return;
        }
        p.sendMessage(CHANGE_PASSWORD + " " + exPassword.getText() + " " + newPassword.getText());
        exPassword.setText("");
        newPassword.setText("");
        repeatPassword.setText("");
    }

}