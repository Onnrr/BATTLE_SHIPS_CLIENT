package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;

public class SignupController {
    final String SUCCESS = "SUCCESS";
    final String FAIL = "FAIL";
    final String INVALID_NAME = "NAME";
    final String INVALID_MAIL = "MAIL";
    final String CREATE = "create";

    @FXML
    TextField nameField;

    @FXML
    TextField mailField;

    @FXML
    TextField passwordField;

    @FXML
    TextField repeatPasswordField;

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
            e1.printStackTrace();
        }
        // TODO check validity
        String command = CREATE + " " + nameField.getText() + " " + passwordField.getText() + " " + mailField.getText();

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
        } else {
            System.out.println(message);
        }

    }

}
