package controllers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.scene.Node;
import models.AppManager;
import models.Chat;
import models.Initialise;
import models.InviteButton;
import models.Notification;
import models.OnlinePlayer;
import models.Player;
import models.Settings;

/**
 * Controls the behaviour of lobby components
 */
public class LobbyController implements Initialise, Runnable {
    final String CONNECTED = "CONNECTED";
    final String DISCONNECTED = "DISCONNECTED";
    final String GAME_FAIL = "GAME_FAIL";
    final String GAME_START = "GAME_START";
    final String DECLINE_GAME = "decline_game";
    final String ACCEPT_GAME = "accept_game";
    final String INVITE = "invite";
    final String DISCONNECT = "disconnect";
    final String INVITATION = "INVITATION";
    final String DECLINED = "DECLINED";
    final String NEW_MESSAGE = "MESSAGE";
    final String STARTED_GAME = "STARTED_GAME";
    final String LEFT_GAME = "LEFT_GAME";
    final String PASSWORD_CHANGED = "PASSWORD_CHANGED";
    final String PASSWORD_CHANGE_FAIL = "PASSWORD_CHANGE_FAIL";

    final int HOME = 0;
    final int CHAT = 1;
    final int SETTINGS = 2;

    @FXML
    Button logoutButton;

    @FXML
    Button homeButton;

    @FXML
    Button settingsButton;

    @FXML
    Button chatButton;

    @FXML
    Text userName;

    @FXML
    VBox notificationsBox;

    @FXML
    VBox rankingBox;

    @FXML
    FlowPane onlinePlayers;

    @FXML
    GridPane root;

    Settings settings;
    Chat chat;

    Socket s;
    Thread t;
    Player p;
    BufferedReader in;
    PrintWriter out;
    Button sendButton;
    TextField field;
    boolean stop;
    int currentScene; // 0 home, 1 chat, 2 settings

    @Override
    /**
     * Initialises lobby with information of the player
     * 
     * @param Player p player object to get current user information
     */
    public void initialise(Player player) {
        stop = false;
        p = player;
        s = p.getSocket();
        userName.setText(p.getName());
        System.out.println(p.getOnlinePlayers());
        start();
        notificationsBox.getChildren().clear();
        currentScene = HOME;
        settings = new Settings(player);

        sendButton = new Button();
        field = new TextField();
        chat = new Chat(player, field, sendButton);

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

        result = p.getRank().split(" ");
        int rank = 1;
        for (int i = 1; i < result.length - 1; i += 2) {
            Notification not = new Notification(rank, result[i], "Score : " + result[i + 1]);
            rankingBox.getChildren().add(not);
            rank++;
        }

    }

    /**
     * Event of the invite buttons. Sends invitation request to server.
     * Also adds pending request notification
     * 
     * @param event
     */
    public void invite(MouseEvent event) {
        String command = "";
        int otherID = ((InviteButton) event.getSource()).getID();
        command += INVITE + " " + otherID;
        p.sendMessage(command);
        ((InviteButton) event.getSource()).setDisable(true);

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
    /**
     * Thread for getting messages from the server
     */
    public void run() {
        String message;
        while (!stop) {
            message = p.getMessage();
            System.out.println(message);
            try {
                execute(message);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Takes the server message as argument and execucutes the command
     * 
     * @param message message from the server
     * @throws IOException
     */
    private void execute(String message) throws IOException {
        if (message == null) {
            return;
        }
        String[] result = message.split(" ");

        if (result[0].equals(CONNECTED)) {
            // Adds connected player to the list
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
            // Removes the disconnected player from the list
            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                if (result[2].equals(((OnlinePlayer) onlinePlayers.getChildren().get(i)).getName())) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            onlinePlayers.getChildren().remove(onlinePlayers.getChildren().get(index));
                        }
                    });
                }
            }
            // Removes notifications of the disconnected player
            int otherID = Integer.parseInt(result[1]);
            for (int i = 0; i < notificationsBox.getChildren().size(); i++) {
                Notification cur = (Notification) notificationsBox.getChildren().get(i);
                if (cur.getID() == otherID) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notificationsBox.getChildren().remove(notificationsBox.getChildren().get(index));
                        }
                    });
                }
            }

        } else if (result[0].equals(INVITATION)) {
            // Adds game invite notification
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

                    accept.setOnMouseClicked(e -> {
                        acceptNotification(e);
                    });
                    notificationsBox.getChildren().add(newNot);
                    System.out.println(notificationsBox.getChildren().size());
                }
            });
        } else if (result[0].equals(DECLINED)) {
            // TODO bug fix deletes any notification not spesifically the pending invite
            int otherID = Integer.parseInt(result[1]);
            for (int i = 0; i < notificationsBox.getChildren().size(); i++) {
                Notification cur = (Notification) notificationsBox.getChildren().get(i);
                if (cur.getID() == otherID) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notificationsBox.getChildren().remove(notificationsBox.getChildren().get(index));
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
        } else if (result[0].equals(GAME_FAIL)) {
            // TODO
            System.out.println("Other player is not available");
        } else if (result[0].equals(GAME_START)) {
            // TODO
            p.settOpponentID(Integer.parseInt(result[1]));
            p.setOpponentName(result[2]);
            System.out.println("Game starting with " + p.getOpponentName());

            int otherID = Integer.parseInt(result[1]);
            for (int i = 0; i < notificationsBox.getChildren().size(); i++) {
                Notification cur = (Notification) notificationsBox.getChildren().get(i);
                if (cur.getID() == otherID) {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            notificationsBox.getChildren().remove(cur);
                        }
                    });
                }
            }
            stop = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Stage) root.getScene().getWindow()).close();
                }
            });
            AppManager.goToSetup(getClass().getResource("/views/setup.fxml"), p);
        } else if (result[0].equals(NEW_MESSAGE)) {
            message = message.replace(result[0], "");
            message = message.replace(result[1], "");
            message = message.replace(result[2], "");
            message = message.trim();
            chat.receiveMessage(result[2], message);
        } else if (result[0].equals(STARTED_GAME)) {
            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                if (((OnlinePlayer) onlinePlayers.getChildren().get(i)).getID() == Integer.parseInt(result[1])) {
                    final Integer index = Integer.valueOf(i);
                    System.out.println("çalışıoo");
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((OnlinePlayer) onlinePlayers.getChildren().get(index)).inGame();
                        }
                    });
                }
            }
        } else if (result[0].equals(LEFT_GAME)) {
            for (int i = 0; i < onlinePlayers.getChildren().size(); i++) {
                if (((OnlinePlayer) onlinePlayers.getChildren().get(i)).getID() == Integer.parseInt(result[1])) {
                    final Integer index = Integer.valueOf(i);
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            ((OnlinePlayer) onlinePlayers.getChildren().get(index)).leftGame();
                        }
                    });
                }
            }
        } else if (result[0].equals(PASSWORD_CHANGED)) {
            System.out.println("password changed");
            // TODO
        } else if (result[0].equals(PASSWORD_CHANGE_FAIL)) {
            System.out.println("password change failed");
            // TODO
        } else {
            // System.out.println(message);
        }
    }

    /**
     * Event of declining invitation using the decline button in notification
     * Sends the server the information that the invitation is declined
     * 
     * @param event
     */
    private void declineNotification(MouseEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notificationsBox.getChildren().remove(((InviteButton) event.getSource()).getRoot());
            }
        });
        p.sendMessage(DECLINE_GAME + " " + p.getID() + " " + ((InviteButton) event.getSource()).getID());
    }

    /**
     * Event of accepting invitation using the accept button in notification
     * Sends the server the information that the invitation is accepted
     * 
     * @param event
     */
    private void acceptNotification(MouseEvent event) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                notificationsBox.getChildren().remove(((InviteButton) event.getSource()).getRoot());
            }
        });
        p.sendMessage(ACCEPT_GAME + " " + p.getID() + " " + ((InviteButton) event.getSource()).getID());
    }

    /**
     * Starts the thread
     */
    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

    /**
     * Changes the main component to home
     * 
     * @param e Event of home button
     */
    public void toHome(ActionEvent e) {
        if (currentScene == HOME) {
            return;
        }
        currentScene = HOME;
        root.getChildren().remove(chat);
        root.getChildren().remove(settings);
        root.add(onlinePlayers, 1, 0);
    }

    /**
     * Changes the main component to settings
     * 
     * @param e Event of settings button
     */
    public void toSettings(ActionEvent e) {
        if (currentScene == SETTINGS) {
            return;
        }
        currentScene = SETTINGS;
        root.getChildren().remove(chat);
        root.getChildren().remove(onlinePlayers);
        root.add(settings, 1, 0);
    }

    /**
     * Changes the main component to chat
     * 
     * @param e Event of chat button
     */
    public void toChat(ActionEvent e) {
        if (currentScene == CHAT) {
            return;
        }
        currentScene = CHAT;
        root.getChildren().remove(onlinePlayers);
        root.getChildren().remove(settings);
        root.add(chat, 1, 0);
    }

    /**
     * Logs out from the game and disconnects from the server.,
     * Also goes back to the login
     * 
     * @param e Event of logout button in bottom left and the one in settings.
     */
    public void logout(ActionEvent e) {
        p.sendMessage(DISCONNECT);
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

}
