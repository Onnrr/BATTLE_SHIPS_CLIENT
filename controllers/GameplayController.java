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
import models.Counter;
import models.Initialise;
import models.Player;

public class GameplayController implements Initialise, Runnable {
    final int TABLE_SIZE = 10;
    final int WIN_POINTS = 100;
    final int LOSE_POINTS = 10;
    final int TURN_DURATION = 60;
    final String DISCONNECTED = "OPPONENT_DISCONNECTED";
    final String LEAVE = "leave";
    final String CAN_LEAVE = "LEAVE";
    final String GUESS = "guess";
    final String OPPONENT_GUESS = "GUESS";
    final String HIT = "hit";
    final String MISS = "miss";
    final String CORRECT_GUESS = "HIT";
    final String INCORRECT_GUESS = "MISS";
    final String WIN = "win";
    final String LOSE = "lose";
    final String SKIP = "SKIP";

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

    @FXML
    Text counterText;

    GridPane opponentTable;
    GridPane myTable;
    Cell[][] buttons;
    Cell[][] myButtons;
    Player p;
    Thread t;
    boolean stop;
    int lastGuessColumn;
    int lastGuessRow;
    Counter counter;

    @Override
    /**
     * Initialises the game with current user's information
     * 
     * @param Player player stores the positions of the ships, opponent information
     *               etc.
     */
    public void initialise(Player player) {
        counter = new Counter(TURN_DURATION, counterText, turnText, player);
        lastGuessColumn = -1;
        lastGuessRow = -1;
        stop = false;
        start();
        p = player;
        remShipsText.setText("Remaining Ships : " + p.getRemaining());
        oppShipsText.setText(p.getOpponentName() + "'s remaining ships : " + p.getOppRemaining());
        buttons = new Cell[TABLE_SIZE][TABLE_SIZE];
        myButtons = new Cell[TABLE_SIZE][TABLE_SIZE];

        if (p.isMyTurn()) {
            turnText.setText("Your Turn");
            counterText.setVisible(true);
            counter.reset();
            counter.start();
        } else {
            counterText.setVisible(false);
            turnText.setText(p.getOpponentName() + "'s Turn");
        }
        setUpGrid(p);

    }

    /**
     * Creates teo grids for your ships and opponent's ships
     * 
     * @param p Stores the positions of the ships used for initialising the grid
     */
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
                    b.setOccupied(true);
                    b.getStyleClass().add("occupied");
                }
                myTable.add(b, y, x);
                myButtons[x][y] = b;
            }
        }

    }

    /**
     * Sends the server the guess as row and column values
     * If it is not the player's turn method returns and nothing happens
     * 
     * @param e event of each button on the grid
     * @throws IOException
     */
    private void handleClick(MouseEvent e) throws IOException {
        if (!p.isMyTurn()) {
            return;
        }
        counter.stop();
        counter.reset();
        counterText.setVisible(false);
        lastGuessRow = ((Cell) e.getSource()).getRow();
        lastGuessColumn = ((Cell) e.getSource()).getColumn();

        String guess = String.valueOf(lastGuessRow);
        guess += " " + lastGuessColumn;
        System.out.println(guess);
        p.sendMessage(GUESS + " " + guess);
        p.incrementMoveCount();
        p.setMyTurn(false);
        turnText.setText(p.getOpponentName() + "'s Turn");

    }

    @Override
    /**
     * Thread for getting messages from the server
     */
    public void run() {
        String message;
        while (!stop) {
            message = p.getMessage();
            execute(message);
        }
        System.out.println("Thread stopped");
    }

    /**
     * Takes the server message as argument and execucutes the command
     * 
     * @param message message from the server
     * @throws IOException
     */
    public void execute(String message) {
        String[] result = message.split(" ");
        System.out.println(message);
        if (message.length() < 1) {
            return;
        }
        if (message.equals(DISCONNECTED)) {
            System.out.println("opponent disconnected");

            String onlinePlayers = p.getMessage();
            p.setOnlinePlayers(onlinePlayers);
            System.out.println("Got new players");
            stop = true;
            counter.stop();
            p.reset();
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
            p.reset();
            counter.stop();
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
        } else if (result[0].equals(OPPONENT_GUESS)) {
            int row = Integer.parseInt(result[1]);
            int column = Integer.parseInt(result[2]);
            if (myButtons[row][column].isOccupied()) {
                p.sendMessage(HIT);
                myButtons[row][column].getStyleClass().add("hit");
                p.decrRemaining();
                remShipsText.setText("Remaining Ships : " + p.getRemaining());
                if (p.getRemaining() == 0) {
                    stop = true;
                    p.setWinner(false);
                    p.setScore(p.getScore() - LOSE_POINTS);
                    if (p.getScore() < 0) {
                        p.setScore(0);
                    }
                    p.sendMessage(LOSE + " " + p.getScore());
                    System.out.println("You lose");
                    AppManager.changeScene(getClass().getResource("/views/result.fxml"), turnText, p);
                }
            } else {
                p.sendMessage(MISS);
                myButtons[row][column].getStyleClass().add("miss");
            }
            p.setMyTurn(true);
            turnText.setText("Your Turn");
            counterText.setVisible(true);
            counter = new Counter(TURN_DURATION, counterText, turnText, p);
            counter.start();
        } else if (result[0].equals(CORRECT_GUESS)) {
            buttons[lastGuessRow][lastGuessColumn].setDisable(true);
            buttons[lastGuessRow][lastGuessColumn].getStyleClass().add("hit");
            p.incrementCorrectGuess();
            oppShipsText.setText(p.getOpponentName() + "'s remaining ships : " + p.getOppRemaining());
            if (p.getOppRemaining() == 0) {
                stop = true;
                p.setWinner(true);
                p.setScore(p.getScore() + WIN_POINTS);
                p.sendMessage(WIN + " " + p.getScore());
                System.out.println("You win");
                AppManager.changeScene(getClass().getResource("/views/result.fxml"), turnText, p);
            }
        } else if (result[0].equals(INCORRECT_GUESS)) {
            buttons[lastGuessRow][lastGuessColumn].setDisable(true);
        } else if (result[0].equals(SKIP)) {
            p.setMyTurn(true);
            turnText.setText("Your Turn");
            counterText.setVisible(true);
            counter = new Counter(TURN_DURATION, counterText, turnText, p);
            counter.start();
        } else {
            System.out.println(message);
        }
    }

    /**
     * Starts the thread
     */
    public void start() {
        System.out.println("Starting game thread");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

}