package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.*;;

public class LoginController implements Initializable, Runnable {
    final String SUCCESS = "SUCCESS";
    final String FAIL = "FAIL";
    final String INVALID_NAME = "NAME";
    final String INVALID_MAIL = "MAIL";
    final String INFO = "INFO";
    final String LOGIN_CHECK = "login";
    final String RESET_PASSWORD = "reset_password";
    final String PASSWORD_RESET = "PASSWORD_RESET";
    final String PASSWORD_RESET_FAIL = "PASSWORD_RESET_FAIL";

    boolean run;
    Socket s;
    BufferedReader in;
    PrintWriter out;
    String message;
    Thread t;
    boolean stop;

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

    @FXML
    GridPane darkPane;

    @FXML
    GridPane resetPane;

    @FXML
    TextField resetMailField;

    @FXML
    Button resetButton;

    @FXML
    CheckBox acceptCheckBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stop = false;
        nameError.setVisible(false);
        passwordError.setVisible(false);

        darkPane.setVisible(false);
        darkPane.setDisable(true);

        resetPane.setVisible(false);
        resetPane.setDisable(true);

        resetButton.setDisable(true);

        nameField.setOnMouseClicked(e -> {
            reset();
        });
        passwordField.setOnMouseClicked(e -> {
            reset();
        });
    }

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
            Player p = new Player(s, in, out, Integer.parseInt(result[1]), result[2], Integer.parseInt(result[3]),
                    result[4],
                    null);
            System.out.println("player created");
            String online = in.readLine();
            p.setOnlinePlayers(online);

            String rank = in.readLine();
            p.setRank(rank);
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

        stage.setWidth(stage.getWidth() + 0.0001);

        stage.setScene(scene);
        stage.show();
    }

    private String hashPassword(String password) {
        // TODO
        return password;
    }

    public void reset() {
        nameError.setVisible(false);
        passwordError.setVisible(false);

        nameField.getStyleClass().remove("invalid");
        passwordField.getStyleClass().remove("invalid");

    }

    public void forgotPassword(ActionEvent e) {
        darkPane.setVisible(true);
        darkPane.setDisable(false);
        resetPane.setVisible(true);
        resetPane.setDisable(false);
    }

    public void resetPassword(ActionEvent e) {
        try {
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            return;
        }
        if (resetMailField.getText().equals("") || !resetMailField.getText().contains("@")) {
            System.out.println("invalid mail");
            return;
        }
        out.println(RESET_PASSWORD + " " + resetMailField.getText());
        darkPane.setVisible(false);
        darkPane.setDisable(true);

        resetPane.setVisible(false);
        resetPane.setDisable(true);

        resetButton.setDisable(true);
        acceptCheckBox.setSelected(false);
    }

    public void acceptCheckBox(ActionEvent e) {
        if (acceptCheckBox.isSelected()) {
            resetButton.setDisable(false);
        } else {
            resetButton.setDisable(true);
        }
    }

    public void closeReset(ActionEvent e) {
        resetMailField.setText("");
        acceptCheckBox.setSelected(false);
        resetButton.setDisable(true);
        darkPane.setVisible(false);
        darkPane.setDisable(true);
        resetPane.setVisible(false);
        resetPane.setDisable(true);
    }

    public void closeReset() {
        resetMailField.setText("");
        acceptCheckBox.setSelected(false);
        resetButton.setDisable(true);
        darkPane.setVisible(false);
        darkPane.setDisable(true);
        resetPane.setVisible(false);
        resetPane.setDisable(true);
    }

    @Override
    public void run() {
        String message = "";
        while (!stop) {
            try {
                message = in.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (message.equals(PASSWORD_RESET)) {
                System.out.println("Success");
                closeReset();
            } else if (message.equals(PASSWORD_RESET_FAIL)) {
                System.out.println("Fail");
                closeReset();
            }
        }
    }

}
