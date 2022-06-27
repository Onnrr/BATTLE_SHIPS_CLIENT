package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Initialise;
import models.OnlinePlayer;
import models.Player;

public class LobbyController implements Initialise, Runnable {
    final String CONNECTED = "CONNECTED";
    final String DISCONNECTED = "DISCONNECTED";

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

    @Override
    public void initialise(Player player) {
        p = player;
        s = p.getSocket();
        userName.setText(p.getName());
        System.out.println(p.getOnlinePlayers());
        start();

        String[] result = p.getOnlinePlayers().split(" ");
        for (int i = 1; i < result.length - 1; i += 2) {
            OnlinePlayer cur = new OnlinePlayer(result[i], Integer.parseInt(result[i + 1]));
            onlinePlayers.getChildren().add(cur);
        }

    }

    @Override
    public void run() {
        String message;
        while (true) {
            message = p.getMessage();
            System.out.println(message);
            execute(message);
        }
    }

    private void execute(String message) {
        String[] result = message.split(" ");
        if (result[0].equals(CONNECTED)) {
            OnlinePlayer newPlayer = new OnlinePlayer(result[1], 0);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    onlinePlayers.getChildren().add(newPlayer);
                }
            });
        } else if (result[0].equals(DISCONNECTED)) {
            System.out.println("burdayız");
            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                if (result[1].equals(((OnlinePlayer) onlinePlayers.getChildren().get(i)).getName())) {
                    final Integer index = Integer.valueOf(i);
                    System.out.println("heyy");
                    // TODOOOOO errorss
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            onlinePlayers.getChildren().remove(onlinePlayers.getChildren().get(index));
                        }
                    });
                }
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

}
