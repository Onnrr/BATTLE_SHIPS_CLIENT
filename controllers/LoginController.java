package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ResourceBundle;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;;

/**
 * Move server connection to login button open app offline
 */
public class LoginController implements Initializable {
    final String SUCCESS = "SUCCESS";
    final String FAIL = "FAIL";
    final String INVALID_NAME = "NAME";
    final String INVALID_MAIL = "MAIL";
    final String INFO = "INFO";
    final String LOGIN_CHECK = "login";

    boolean run;
    Socket s;
    BufferedReader in;
    PrintWriter out;
    String message;

    @FXML
    TextField nameField;

    @FXML
    TextField passwordField;

    @FXML
    GridPane root;

    @FXML
    Text passwordError;

    @FXML
    Text nameError;

    public void login(ActionEvent e) throws IOException {
        if (nameField.getText().equals("")) {
            nameError.setVisible(true);
            nameError.setText("User name cannot be empty");
            nameField.getStyleClass().add("invalid");
            return;
        }
        if (passwordField.getText().equals("")) {
            passwordError.setVisible(true);
            passwordError.setText("Password cannot be empty");
            passwordField.getStyleClass().add("invalid");
            return;
        }
        try {
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            // TODO
            return;
        }

        String hashed = hashPassword(passwordField.getText());
        System.out.println(hashed);
        String command = LOGIN_CHECK + " " + nameField.getText() + " " + hashed;

        out.println(command);

        String message = "";
        try {
            message = in.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String[] result = message.split(" ");
        if (result[0].equals(FAIL)) {
            passwordError.setText("Wrong user name or password");
            passwordError.setVisible(true);
            passwordField.getStyleClass().add("invalid");
        } else if (result[0].equals(INFO)) {
            System.out.println("Login successful");
            Player p = new Player(s, Integer.parseInt(result[1]), result[2], Integer.parseInt(result[3]), result[4],
                    null);
            AppManager.changeScene(getClass().getResource("/views/lobby.fxml"), e, p);
        } else {
            System.out.println(message);
        }
    }

    public void register(ActionEvent e) {
        root.getColumnConstraints().clear();
        root.getRowConstraints().clear();
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/views/signup.fxml"));
        try {
            loader.load();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        Parent p = loader.getRoot();
        Stage stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
        Scene scene = new Scene(p, ((Node) e.getSource()).getScene().getWidth(),
                ((Node) e.getSource()).getScene().getHeight());

        // stage.setHeight(stage.getHeight() + 0.0001);
        stage.setWidth(stage.getWidth() + 0.0001);

        stage.setScene(scene);
        stage.show();
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
        nameError.setVisible(false);
        passwordError.setVisible(false);

        nameField.setOnMouseClicked(e -> {
            reset();
        });
        passwordField.setOnMouseClicked(e -> {
            reset();
        });
    }

    public void reset() {
        nameError.setVisible(false);
        passwordError.setVisible(false);

        nameField.getStyleClass().remove("invalid");
        passwordField.getStyleClass().remove("invalid");

    }
}
