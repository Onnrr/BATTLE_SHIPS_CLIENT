package controllers;

import java.io.BufferedReader;

import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;

import javafx.fxml.FXML;

import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import models.Initialise;
import models.InviteButton;
import models.Notification;
import models.OnlinePlayer;
import models.Player;

public class LobbyController implements Initialise, Runnable {
    final String CONNECTED = "CONNECTED";
    final String DISCONNECTED = "DISCONNECTED";
    final String DECLINE_GAME = "decline_game";
    final String INVITE = "invite";
    final String INVITATION = "INVITATION";
    final String DECLINED = "DECLINED";

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
        notificationsBox.getChildren().clear();

        String[] result = p.getOnlinePlayers().split(" ");
        for (int i = 1; i < result.length - 2; i += 3) {
            InviteButton inviteButton = new InviteButton(Integer.parseInt(result[i]), result[i + 1]);
            inviteButton.setOnMouseClicked(e -> {
                invite(e);
            });
            OnlinePlayer cur = new OnlinePlayer(result[i + 1], Integer.parseInt(result[i]),
                    Integer.parseInt(result[i + 2]), inviteButton);
            inviteButton.setRoot(cur);
            onlinePlayers.getChildren().add(cur);
        }
    }

    public void invite(MouseEvent event) {
        String command = "";
        int otherID = ((InviteButton) event.getSource()).getID();
        command += INVITE + " " + otherID;
        p.sendMessage(command);
        ((InviteButton) event.getSource()).setDisable(true);
        // TODO enable when declined

        Notification newNot = new Notification("Pending invite",
                "Waiting for " + ((InviteButton) event.getSource()).getName() + "'s response", otherID);
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notificationsBox.getChildren().add(newNot);
                System.out.println(notificationsBox.getChildren().size());
            }
        });
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
            InviteButton inviteButton = new InviteButton(Integer.parseInt(result[1]), result[2]);
            inviteButton.setOnMouseClicked(e -> {
                invite(e);
            });
            OnlinePlayer newPlayer = new OnlinePlayer(result[2], Integer.parseInt(result[1]), 0, inviteButton);
            inviteButton.setRoot(newPlayer);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    onlinePlayers.getChildren().add(newPlayer);
                }
            });
        } else if (result[0].equals(DISCONNECTED)) {
            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                if (result[1].equals(((OnlinePlayer) onlinePlayers.getChildren().get(i)).getName())) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            onlinePlayers.getChildren().remove(onlinePlayers.getChildren().get(index));
                        }
                    });
                }
            }
        } else if (result[0].equals(INVITATION)) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    InviteButton accept = new InviteButton(Integer.parseInt(result[1]), result[2]);
                    InviteButton decline = new InviteButton(Integer.parseInt(result[1]), result[2]);
                    Notification newNot = new Notification("Game Invite", result[2] + " has invited you", accept,
                            decline, Integer.parseInt(result[1]));
                    accept.setRoot(newNot);
                    decline.setRoot(newNot);
                    decline.setOnMouseClicked(e -> {
                        declineNotification(e);
                    });
                    notificationsBox.getChildren().add(newNot);
                    System.out.println(notificationsBox.getChildren().size());
                }
            });
        } else if (result[0].equals(DECLINED)) {
            int otherID = Integer.parseInt(result[1]);
            for (int i = 0; i < notificationsBox.getChildren().size(); i++) {
                System.out.println("aa");
                Notification cur = (Notification) notificationsBox.getChildren().get(i);
                if (cur.getID() == otherID) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notificationsBox.getChildren().remove(notificationsBox.getChildren().get(index));
                            System.out.println(notificationsBox.getChildren().size());
                            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                                if (((OnlinePlayer) onlinePlayers.getChildren().get(i)).getID() == otherID) {
                                    ((OnlinePlayer) onlinePlayers.getChildren().get(i)).getInviteButton()
                                            .setDisable(false);
                                }
                            }
                        }
                    });
                }
            }
        } else {
            System.out.println(message);
        }
    }

    private void declineNotification(MouseEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notificationsBox.getChildren().remove(((InviteButton) event.getSource()).getRoot());
            }
        });
        p.sendMessage(DECLINE_GAME + " " + p.getID() + " " + ((InviteButton) event.getSource()).getID());
    }

    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

}
