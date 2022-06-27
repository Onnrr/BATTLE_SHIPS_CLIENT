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

import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignupController implements Initializable {
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

    Socket s;
    BufferedReader in;
    PrintWriter out;
    String message;

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
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            // TODO
            return;
        }

        int precheck = valid();

        if (precheck == NAME_EMPTY) {
            nameError.setVisible(true);
            nameError.setText("User name cannot be empty");
            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == NAME_TOO_SHORT) {
            nameError.setVisible(true);
            nameError.setText("User name is too short");
            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == NAME_TOO_LONG) {
            nameError.setVisible(true);
            nameError.setText("User name is too long");
            nameField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == MAIL_EMPTY) {
            mailError.setVisible(true);
            mailError.setText("Mail cannot be empty");
            mailField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == INVALID_MAIL) {
            mailError.setVisible(true);
            mailError.setText("Invalid mail");
            mailField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_EMPTY) {
            passwordError.setVisible(true);
            passwordError.setText("Password cannot be empty");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_TOO_SHORT) {
            passwordError.setVisible(true);
            passwordError.setText("Password is too short");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_TOO_LONG) {
            passwordError.setVisible(true);
            passwordError.setText("Password is too long");
            passwordField.getStyleClass().add("invalid");
            out.println("disconnect");
            return;
        } else if (precheck == PASSWORD_MATCH) {
            passwordError.setVisible(true);
            passwordError.setText("Passwords do not match");
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
            System.out.println("Account created");
        } else if (message.equals(FAIL)) {
            System.out.println("Failed");

            nameField.getStyleClass().add("invalid");
            mailError.setVisible(true);
            mailError.setText("User name or mail is already used");
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
        if (nameField.getText().length() < 4) {
            return NAME_TOO_SHORT;
        }
        if (nameField.getText().length() > 8) {
            return NAME_TOO_LONG;
        }
        if (mailField.getText().equals("")) {
            return MAIL_EMPTY;
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
        if (!mailField.getText().contains("@")) {
            return INVALID_MAIL;
        }
        return VALID;
    }

    public void reset() {
        nameError.setVisible(false);
        mailError.setVisible(false);
        passwordError.setVisible(false);

        nameField.getStyleClass().remove("invalid");
        mailField.getStyleClass().remove("invalid");
        passwordField.getStyleClass().remove("invalid");

    }

    private String hashPassword(String password) {
        // SecureRandom random = new SecureRandom();
        // byte[] salt = new byte[16];
        // random.nextBytes(salt);

        // KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 65536, 128);
        // SecretKeyFactory factory = null;

        // byte[] hash = {};
        // try {
        // factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        // } catch (NoSuchAlgorithmException e2) {
        // e2.printStackTrace();
        // }
        // try {
        // hash = factory.generateSecret(spec).getEncoded();
        // } catch (InvalidKeySpecException e2) {
        // e2.printStackTrace();
        // }
        // TODO
        return password;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        nameField.setOnMouseClicked(e -> {
            reset();
        });
        passwordField.setOnMouseClicked(e -> {
            reset();
        });
        mailField.setOnMouseClicked(e -> {
            reset();
        });
        repeatPasswordField.setOnMouseClicked(e -> {
            reset();
        });

    }
}
