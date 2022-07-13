package controllers;

import java.io.IOException;

import javafx.application.Platform;
import javafx.stage.Stage;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.text.Text;
import models.AppManager;
import models.Initialise;
import models.Player;

public class ResultController implements Initialise, Runnable {
    final String GAME_FINISHED = "game_finished";
    final String ONLINE_PLAYERS = "ONLINE_PLAYERS";
    final String RANK = "RANK";

    boolean stop;
    Thread t;

    @FXML
    Text result;

    @FXML
    Text text;

    @FXML
    Text scoreUpdate;

    @FXML
    Text newScore;

    Player p;

    @Override
    public void initialise(Player player) {
        stop = false;
        start();
        p = player;
        if (p.isWinner()) {
            result.setText("YOU WIN!!");
            text.setText("You sunk all of " + p.getOpponentName() + "'s ships in " + p.getMoveCount() + " turns");
            scoreUpdate.setText("Score + 100");
            newScore.setText("New Score : " + p.getScore());
        } else {
            result.setText("YOU LOSE");
            text.setText("You lasted " + p.getMoveCount() + " turns against " + p.getOpponentName());
            scoreUpdate.setText("Score - 10");
            newScore.setText("New Score : " + p.getScore());
        }
    }

    public void toLobby(ActionEvent e) {
        p.reset();
        p.sendMessage(GAME_FINISHED);
    }

    @Override
    public void run() {
        String message;
        while (!stop) {
            message = p.getMessage();
            execute(message);
        }

    }

    private void execute(String message) {
        String[] result = message.split(" ");
        if (result[0].equals(ONLINE_PLAYERS)) {
            p.setOnlinePlayers(message);
        } else if (result[0].equals(RANK)) {
            p.setRank(message);
            stop = true;
            try {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        ((Stage) text.getScene().getWindow()).close();
                    }
                });
                AppManager.goToLobby(getClass().getResource("/views/lobby.fxml"), p);
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public void start() {
        System.out.println("Starting result thread");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

}
