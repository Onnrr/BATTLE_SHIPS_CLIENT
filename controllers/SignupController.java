package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import java.net.Socket;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignupController implements Initializable {
    final String IP = "localhost";

    final String SUCCESS = "SUCCESS";
    final String FAIL = "FAIL";
    final String CREATE = "create";

    final int VALID = 0;
    final int NAME_TOO_LONG = 1;
    final int NAME_EMPTY = 2;
    final int MAIL_EMPTY = 3;
    final int PASSWORD_EMPTY = 4;
    final int PASSWORD_MATCH = 5;
    final int NAME_TOO_SHORT = 6;
    final int PASSWORD_TOO_SHORT = 7;
    final int PASSWORD_TOO_LONG = 8;
    final int INVALID_MAIL = 9;

    @FXML
    TextField nameField;

    @FXML
    TextField mailField;

    @FXML
    TextField passwordField;

    @FXML
    TextField repeatPasswordField;

    @FXML
    Text nameError;

    @FXML
    Text mailError;

    @FXML
    Text passwordError;

    @FXML
    Text nameTag;

    @FXML
    Text mailTag;

    @FXML
    Text passwordTag;

    @FXML
    Text repeatPasswordTag;

    @FXML
    Pane nameBack;

    @FXML
    Pane mailBack;

    @FXML
    Pane passwordBack;

    @FXML
    Pane repeatPasswordBack;

    Socket s;
    BufferedReader in;
    PrintWriter out;
    String message;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        nameBack.setVisible(true);
        mailBack.setVisible(false);
        passwordBack.setVisible(false);
        repeatPasswordBack.setVisible(false);

        nameField.setOnMouseClicked(e -> {
            reset();
            nameTag.setText("User Name");
            nameBack.setMaxWidth(100);
            nameBack.setVisible(true);
            mailBack.setVisible(false);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(false);
        });
        passwordField.setOnMouseClicked(e -> {
            reset();
            passwordTag.setText("Password");
            passwordBack.setMaxWidth(80);
            nameBack.setVisible(false);
            mailBack.setVisible(false);
            passwordBack.setVisible(true);
            repeatPasswordBack.setVisible(false);
        });
        mailField.setOnMouseClicked(e -> {
            reset();
            mailTag.setText("Mail");
            mailBack.setMaxWidth(50);
            nameBack.setVisible(false);
            mailBack.setVisible(true);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(false);
        });
        repeatPasswordField.setOnMouseClicked(e -> {
            reset();
            repeatPasswordTag.setText("Repeat password");
            repeatPasswordBack.setMaxWidth(130);
            nameBack.setVisible(false);
            mailBack.setVisible(false);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(true);
        });

        nameField.setOnDragDetected(e -> {
            reset();
            nameTag.setText("User Name");
            nameBack.setMaxWidth(100);
            nameBack.setVisible(true);
            mailBack.setVisible(false);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(false);
        });
        passwordField.setOnDragDetected(e -> {
            reset();
            passwordTag.setText("Password");
            passwordBack.setMaxWidth(80);
            nameBack.setVisible(false);
            mailBack.setVisible(false);
            passwordBack.setVisible(true);
            repeatPasswordBack.setVisible(false);
        });
        mailField.setOnDragDetected(e -> {
            reset();
            mailTag.setText("Mail");
            mailBack.setMaxWidth(50);
            nameBack.setVisible(false);
            mailBack.setVisible(true);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(false);
        });
        repeatPasswordField.setOnDragDetected(e -> {
            reset();
            repeatPasswordTag.setText("Repeat password");
            repeatPasswordBack.setMaxWidth(130);
            nameBack.setVisible(false);
            mailBack.setVisible(false);
            passwordBack.setVisible(false);
            repeatPasswordBack.setVisible(true);
        });

    }

    public void haveAnAccount(ActionEvent e) {
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

    public void signup(ActionEvent e) {
        try {
            s = new Socket(IP, 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            // TODO
            return;
        }

        int precheck = valid();

        if (precheck == NAME_EMPTY) {
            nameBack.setVisible(true);
            nameBack.setMaxWidth(230);
            nameTag.setText("User name cannot be empty");
            nameTag.getStyleClass().add("errortext");
            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == NAME_TOO_SHORT) {
            nameBack.setVisible(true);
            nameBack.setMaxWidth(190);
            nameTag.setText("User name is too short");
            nameTag.getStyleClass().add("errortext");

            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == NAME_TOO_LONG) {
            nameBack.setVisible(true);
            nameBack.setMaxWidth(190);
            nameTag.setText("User name is too long");
            nameTag.getStyleClass().add("errortext");

            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == MAIL_EMPTY) {
            mailBack.setVisible(true);
            mailBack.setMaxWidth(180);
            mailTag.setText("Mail cannot be empty");
            mailTag.getStyleClass().add("errortext");
            mailField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == INVALID_MAIL) {
            mailBack.setVisible(true);
            mailBack.setMaxWidth(120);
            mailTag.setText("Invalid Mail");
            mailTag.getStyleClass().add("errortext");
            mailField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_EMPTY) {
            passwordBack.setVisible(true);
            passwordBack.setMaxWidth(200);
            passwordTag.setText("Password cannot be empty");
            passwordTag.getStyleClass().add("errortext");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_TOO_SHORT) {
            passwordBack.setVisible(true);
            passwordBack.setMaxWidth(180);
            passwordTag.setText("Password is too short");
            passwordTag.getStyleClass().add("errortext");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_TOO_LONG) {
            passwordBack.setVisible(true);
            passwordBack.setMaxWidth(180);
            passwordTag.setText("Password is too long");
            passwordTag.getStyleClass().add("errortext");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_MATCH) {
            passwordBack.setVisible(true);
            passwordBack.setMaxWidth(220);
            passwordTag.setText("Passwords do not match");
            passwordTag.getStyleClass().add("errortext");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        }

        String hashed = hashPassword(passwordField.getText());

        String command = CREATE + " " + nameField.getText() + " " + hashed + " " + mailField.getText();

        out.println(command);
        String message = "";
        try {
            message = in.readLine();
        } catch (IOException e2) {
            e2.printStackTrace();
        }

        if (message.equals(SUCCESS)) {
            reset();
            System.out.println("Account created");
            mailField.setText("");
            nameField.setText("");
            passwordField.setText("");
            repeatPasswordField.setText("");
            nameField.getStyleClass().add("success");
        } else if (message.equals(FAIL)) {
            System.out.println("Failed");

            mailBack.setVisible(true);
            mailBack.setMaxWidth(120);
            mailTag.setText("User name or mail is already used");
            mailTag.getStyleClass().add("errortext");

            mailField.getStyleClass().add("invalid");
            out.println("disconnect");
        } else {
            System.out.println(message);
        }
    }

    private int valid() {
        if (nameField.getText().equals("")) {
            return NAME_EMPTY;
        }
        if (nameField.getText().length() < 3) {
            return NAME_TOO_SHORT;
        }
        if (nameField.getText().length() > 16) {
            return NAME_TOO_LONG;
        }
        if (mailField.getText().equals("")) {
            return MAIL_EMPTY;
        }
        if (!mailField.getText().contains("@")) {
            return INVALID_MAIL;
        }
        if (passwordField.getText().equals("")) {
            return PASSWORD_EMPTY;
        }
        if (passwordField.getText().length() < 4) {
            return PASSWORD_TOO_SHORT;
        }
        if (passwordField.getText().length() > 16) {
            return PASSWORD_TOO_LONG;
        }
        if (!passwordField.getText().equals(repeatPasswordField.getText())) {
            return PASSWORD_MATCH;
        }

        return VALID;
    }

    public void reset() {
        nameError.setVisible(false);
        mailError.setVisible(false);
        passwordError.setVisible(false);
        mailBack.setMaxWidth(50);
        nameBack.setMaxWidth(100);
        passwordBack.setMaxWidth(80);
        repeatPasswordBack.setMaxWidth(130);
        nameField.getStyleClass().remove("invalid");
        mailField.getStyleClass().remove("invalid");
        passwordField.getStyleClass().remove("invalid");
        nameTag.getStyleClass().remove("errortext");
        passwordTag.getStyleClass().remove("errortext");
        mailTag.getStyleClass().remove("errortext");
        repeatPasswordTag.getStyleClass().remove("errortext");
        nameBack.setVisible(false);
        mailBack.setVisible(false);
        passwordBack.setVisible(false);
        repeatPasswordBack.setVisible(false);

    }

    private String hashPassword(String password) {
        // TODO
        return password;
    }

    public void nameTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            nameBack.setVisible(false);
            mailTag.setVisible(true);
            mailBack.setVisible(true);
            mailBack.setMaxWidth(50);
        }
    }

    public void mailTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            mailBack.setVisible(false);
            passwordTag.setVisible(true);
            passwordBack.setVisible(true);
            passwordBack.setMaxWidth(80);
        }
    }

    public void passwordTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            passwordBack.setVisible(false);
            repeatPasswordTag.setVisible(true);
            repeatPasswordBack.setVisible(true);
            repeatPasswordBack.setMaxWidth(130);
        }
    }

    public void repeatPasswordTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            repeatPasswordBack.setVisible(false);
        }
    }

}
