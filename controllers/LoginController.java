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
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
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

    @FXML
    Pane textBack1;

    @FXML
    Pane textBack2;

    @FXML
    Text nameTag;

    @FXML
    Text passwordTag;

    @FXML
    Text mailTag;

    @FXML
    Pane mailBack;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        stop = false;
        nameError.setVisible(false);
        passwordError.setVisible(false);

        nameField.deselect();

        mailBack.setVisible(false);

        textBack1.setMaxWidth(75);
        textBack2.setMaxWidth(90);

        nameTag.setVisible(true);
        passwordTag.setVisible(false);

        textBack1.setVisible(false);

        darkPane.setVisible(false);
        darkPane.setDisable(true);

        resetPane.setVisible(false);
        resetPane.setDisable(true);

        resetButton.setDisable(true);

        nameField.setOnMouseClicked(e -> {
            reset();
            nameTag.setVisible(true);
            textBack2.setVisible(true);
            textBack2.setMaxWidth(90);
        });

        passwordField.setOnMouseClicked(e -> {
            reset();
            passwordTag.setVisible(true);
            textBack1.setVisible(true);
            textBack1.setMaxWidth(75);
        });

        nameField.setOnDragDetected(e -> {
            reset();
            nameTag.setVisible(true);
            textBack2.setVisible(true);
            textBack2.setMaxWidth(90);
        });

        passwordField.setOnDragDetected(e -> {
            reset();
            passwordTag.setVisible(true);
            textBack1.setVisible(true);
            textBack1.setMaxWidth(75);
        });

        resetMailField.setOnMouseClicked(e -> {
            mailTag.setText("Mail");
            mailBack.setMaxWidth(50);
            mailBack.setMinWidth(50);
            mailTag.getStyleClass().remove("errortext");
            resetMailField.getStyleClass().remove("invalid");
            mailBack.setVisible(true);
        });

        resetMailField.setOnDragDetected(e -> {
            mailTag.setText("Mail");
            mailBack.setMaxWidth(50);
            mailBack.setMinWidth(50);
            mailTag.getStyleClass().remove("errortext");
            resetMailField.getStyleClass().remove("invalid");
            mailBack.setVisible(true);
        });
    }

    public void login(ActionEvent e) throws IOException {
        textBack1.setMaxWidth(220);
        textBack2.setMaxWidth(220);
        if (nameField.getText().equals("")) {
            nameError.setVisible(true);
            textBack2.setVisible(true);
            textBack1.setVisible(false);
            passwordTag.setVisible(false);
            nameTag.setVisible(false);
            nameError.setText("User name cannot be empty");
            nameField.getStyleClass().add("invalid");
            return;
        }
        if (passwordField.getText().equals("")) {
            passwordError.setVisible(true);
            textBack1.setVisible(true);
            textBack2.setVisible(false);
            passwordTag.setVisible(false);
            nameTag.setVisible(false);
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
            textBack1.setMaxWidth(225);
            passwordError.setText("Cannot connect to the server");
            passwordError.setVisible(true);
            textBack1.setVisible(true);
            textBack2.setVisible(false);
            passwordTag.setVisible(false);
            nameTag.setVisible(false);
            passwordField.getStyleClass().add("invalid");
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
            textBack1.setMaxWidth(240);
            passwordError.setText("Wrong user name or password");
            passwordError.setVisible(true);
            textBack1.setVisible(true);
            textBack2.setVisible(false);
            passwordTag.setVisible(false);
            nameTag.setVisible(false);
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
        textBack1.setVisible(false);
        textBack2.setVisible(false);

        nameField.getStyleClass().remove("invalid");
        passwordField.getStyleClass().remove("invalid");

    }

    public void forgotPassword(ActionEvent e) {
        reset();
        darkPane.setVisible(true);
        darkPane.setDisable(false);
        resetPane.setVisible(true);
        resetPane.setDisable(false);
    }

    public void resetPassword(ActionEvent e) {
        if (resetMailField.getText().equals("") || !resetMailField.getText().contains("@")) {
            mailTag.setText("Invalid mail");
            mailBack.setMinWidth(100);
            mailTag.getStyleClass().add("errortext");
            resetMailField.getStyleClass().add("invalid");
            mailBack.setVisible(true);
            System.out.println("invalid mail");
            return;
        }
        try {
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            mailTag.setText("Connection failed");
            mailBack.setMinWidth(150);
            mailTag.getStyleClass().add("errortext");
            resetMailField.getStyleClass().add("invalid");
            mailBack.setVisible(true);
            System.out.println("Failed due to connection");
            return;
        }

        start();
        out.println(RESET_PASSWORD + " " + resetMailField.getText());

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
        mailTag.getStyleClass().remove("errortext");
        resetMailField.getStyleClass().remove("invalid");
        textBack1.setMaxWidth(75);
        textBack2.setMaxWidth(90);

        nameTag.setVisible(true);
        passwordTag.setVisible(false);

        mailBack.setVisible(false);

        textBack1.setVisible(false);
        textBack2.setVisible(true);
        resetMailField.setText("");
        acceptCheckBox.setSelected(false);
        resetButton.setDisable(true);
        darkPane.setVisible(false);
        darkPane.setDisable(true);
        resetPane.setVisible(false);
        resetPane.setDisable(true);
    }

    public void closeReset() {
        mailTag.getStyleClass().remove("errortext");
        resetMailField.getStyleClass().remove("invalid");
        mailBack.setVisible(false);

        textBack1.setMaxWidth(75);
        textBack2.setMaxWidth(90);

        nameTag.setVisible(true);
        passwordTag.setVisible(false);

        textBack1.setVisible(false);
        textBack2.setVisible(true);
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
                out.println("disconnect");
                System.out.println("Success");
                closeReset();
                stop = true;
            } else if (message.equals(PASSWORD_RESET_FAIL)) {
                out.println("disconnect");
                System.out.println("Fail");
                mailTag.setText("No account found with mail");
                mailBack.setMinWidth(200);
                mailTag.getStyleClass().add("errortext");
                resetMailField.getStyleClass().add("invalid");
                mailBack.setVisible(true);

                stop = true;
            }
        }
    }

    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

    public void nameFieldTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            nameTag.setVisible(false);
            textBack2.setVisible(false);
            passwordTag.setVisible(true);
            textBack1.setVisible(true);
            textBack1.setMaxWidth(75);
        }
    }

    public void passwordFieldTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            passwordTag.setVisible(false);
            textBack1.setVisible(false);
        }
    }

    public void registerTab(KeyEvent event) {
        if (event.getCode() == KeyCode.TAB) {
            nameTag.setVisible(true);
            textBack2.setVisible(true);
            textBack2.setMaxWidth(90);
        }
    }

}
