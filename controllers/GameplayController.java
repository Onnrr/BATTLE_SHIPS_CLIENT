package controllers;

import java.io.IOException;

import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import models.AppManager;
import models.Cell;
import models.Initialise;
import models.Player;

public class GameplayController implements Initialise, Runnable {
    final int TABLE_SIZE = 10;
    final String DISCONNECTED = "OPPONENT_DISCONNECTED";
    final String LEAVE = "leave";
    final String CAN_LEAVE = "LEAVE";

    @FXML
    GridPane rightGrid;

    @FXML
    AnchorPane anchor;

    @FXML
    AnchorPane anchor2;

    @FXML
    Text turnText;

    @FXML
    Text oppShipsText;

    @FXML
    Text remShipsText;

    GridPane opponentTable;
    GridPane myTable;
    Cell[][] buttons;
    Player p;
    Thread t;
    boolean stop;

    @Override
    public void initialise(Player player) {
        stop = false;
        p = player;
        remShipsText.setText("Remaining Ships : " + p.getRemaining());
        oppShipsText.setText("Opponent's remaining ships : " + p.getOppRemaining());
        buttons = new Cell[TABLE_SIZE][TABLE_SIZE];

        setUpGrid(p);

        // TODO Turn text
    }

    public void setUpGrid(Player p) {
        opponentTable = new GridPane();
        opponentTable.setMaxHeight(Double.MAX_VALUE);
        opponentTable.setMaxWidth(Double.MAX_VALUE);

        myTable = new GridPane();
        myTable.setMaxHeight(Double.MAX_VALUE);
        myTable.setMaxWidth(Double.MAX_VALUE);

        AnchorPane.setRightAnchor(opponentTable, 10.0);
        AnchorPane.setLeftAnchor(opponentTable, 30.0);
        AnchorPane.setBottomAnchor(opponentTable, 10.0);
        AnchorPane.setTopAnchor(opponentTable, 10.0);
        anchor.getChildren().add(opponentTable);

        rightGrid.add(myTable, 1, 1);

        for (int i = 0; i < TABLE_SIZE; i++) {
            ColumnConstraints col = new ColumnConstraints();
            ColumnConstraints col2 = new ColumnConstraints();
            col.setPercentWidth(100 / TABLE_SIZE);
            col2.setPercentWidth(100 / TABLE_SIZE);
            opponentTable.getColumnConstraints().add(col);
            myTable.getColumnConstraints().add(col2);
            RowConstraints row = new RowConstraints();
            RowConstraints row2 = new RowConstraints();
            row.setPercentHeight(100 / TABLE_SIZE);
            row2.setPercentHeight(100 / TABLE_SIZE);
            opponentTable.getRowConstraints().add(row);
            myTable.getRowConstraints().add(row2);
        }

        for (int x = 0; x < TABLE_SIZE; x++) {
            for (int y = 0; y < TABLE_SIZE; y++) {
                Cell button = new Cell(x, y);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);

                button.setOnMouseClicked(e -> {
                    try {
                        handleClick(e);
                    } catch (IOException e1) {
                        System.out.println("mouseclick IO error");
                    }
                });
                opponentTable.add(button, y, x);
                buttons[x][y] = button;

                Cell b = new Cell(x, y);
                b.setMaxWidth(Double.MAX_VALUE);
                b.setMaxHeight(Double.MAX_VALUE);
                b.setDisable(true);

                if (p.getMyTable()[x][y] == 1) {
                    b.getStyleClass().add("occupied");
                }
                myTable.add(b, y, x);
            }
        }

    }

    private void handleClick(MouseEvent e) throws IOException {
        int row = ((Cell) e.getSource()).getRow();
        int column = ((Cell) e.getSource()).getColumn();

        String guess = String.valueOf(row);
        guess += column;
        System.out.println(guess);
        // p.sendMessage(guess);

        // TODO
    }

    @Override
    public void run() {
        String message;
        while (!stop) {
            message = p.getMessage();
            execute(message);
        }
        System.out.println("Thread stopped");
    }

    public void execute(String message) {
        String[] result = message.split(" ");
        if (message.equals(DISCONNECTED)) {
            System.out.println("opponent disconnected");

            String onlinePlayers = p.getMessage();
            p.setOnlinePlayers(onlinePlayers);
            System.out.println("Got new players");
            stop = true;
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Stage) turnText.getScene().getWindow()).close();
                    try {
                        AppManager.goToLobby(getClass().getResource("/views/lobby.fxml"), p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (result[0].equals(CAN_LEAVE)) {
            String onlinePlayers = p.getMessage();
            System.out.println("Got message " + onlinePlayers);
            p.setOnlinePlayers(onlinePlayers);
            System.out.println("Got new players");
            stop = true;
            // counter.stop();
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Stage) turnText.getScene().getWindow()).close();
                    try {
                        AppManager.goToLobby(getClass().getResource("/views/lobby.fxml"), p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

}