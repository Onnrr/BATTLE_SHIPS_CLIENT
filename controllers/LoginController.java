package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.*;;

/**
 * Move server connection to login button open app offline
 */
public class LoginController {
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

    public void login(ActionEvent e) throws IOException {
        if (nameField.getText().equals("") || passwordField.getText().equals("")) {
            return;
            // TODO update gui
        }

        try {
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            e1.printStackTrace();
        }

        String command = LOGIN_CHECK + " " + nameField.getText() + " " + passwordField.getText();

        out.println(command);

        String message = "";
        try {
            message = in.readLine();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        String[] result = message.split(" ");
        if (result[0].equals(FAIL)) {
            System.out.println("Failed");
            // TODO update gui
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
        stage.setScene(scene);
        stage.show();
    }

}
