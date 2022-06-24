package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.Buffer;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Initialise;
import models.Player;

public class LobbyController implements Initialise, Runnable {
    @FXML
    Text userName;

    @FXML
    VBox notificationsBox;

    @FXML
    VBox rankingBox;

    @FXML
    FlowPane onlinePlayers;

    Socket s;
    Thread t;
    Player p;
    BufferedReader in;
    PrintWriter out;
    String message;

    @Override
    public void initialise(Player p) {
        this.p = p;
        s = p.getSocket();
        userName.setText(p.getName());
        try {
            s = new Socket("localhost", 9999);
            in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            out = new PrintWriter(s.getOutputStream(), true);
        } catch (IOException e1) {
            System.out.println("Failed due to connection");
            e1.printStackTrace();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                message = in.readLine();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            execute(message);
        }
    }

    private void execute(String message) {

    }

    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

}
