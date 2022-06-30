package controllers;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import models.AppManager;
import models.Cell;
import models.Initialise;
import models.Player;

public class SetupController implements Runnable, Initialise {
    final String DISCONNECTED = "OPPONENT_DISCONNECTED";
    final String READY = "OPPONENT_READY";
    final int TABLE_SIZE = 10;
    int length;
    boolean isVertical;
    Cell[][] buttons;
    Player player;

    @FXML
    GridPane background;

    @FXML
    Button readyButton;

    @FXML
    ComboBox<String> shipBox;

    @FXML
    ComboBox<String> directionBox;

    @FXML
    ImageView image;

    @FXML
    Text myUserName;

    @FXML
    Text opponentUserName;

    GridPane table;

    boolean noAction;

    boolean stop;
    boolean opponentReady;
    Player p;
    Thread t;

    @Override
    public void initialise(Player player) {
        stop = false;
        p = player;
        start();
        myUserName.setText(p.getName());
        opponentUserName.setText(p.getOpponentName());

        shipBox.getItems().addAll("Boat (1 Block)", "Destroyer (2 Blocks)", "Destroyer (2 Blocks)",
                "Cruiser (3 Blocks)", "Cruiser (3 Blocks)", "BattleShip (4 Blocks)", "Carrier (5 Blocks)");
        directionBox.getItems().addAll("Vertical", "Horizontal");

        shipBox.getSelectionModel().selectFirst();
        directionBox.getSelectionModel().selectFirst();

        isVertical = true;
        length = 1;

        File file = new File("images/ships/1Block.jpg");
        Image img = new Image(file.toURI().toString());
        image.setImage(img);

        buttons = new Cell[TABLE_SIZE][TABLE_SIZE];
        readyButton.setDisable(true);

        noAction = false;
        opponentReady = false;

        setUpGrid(p);
    }

    public void setUpGrid(Player p) {
        table = new GridPane();
        table.setMaxHeight(Double.MAX_VALUE);
        table.setMaxWidth(Double.MAX_VALUE);

        background.add(table, 1, 1);

        for (int i = 0; i < TABLE_SIZE; i++) {
            ColumnConstraints col = new ColumnConstraints();
            col.setPercentWidth(100 / TABLE_SIZE);
            table.getColumnConstraints().add(col);
            RowConstraints row = new RowConstraints();
            row.setPercentHeight(100 / TABLE_SIZE);
            table.getRowConstraints().add(row);
        }

        for (int i = 0; i < TABLE_SIZE; i++) {
            for (int j = 0; j < TABLE_SIZE; j++) {
                Cell button = new Cell(i, j);
                button.setMaxWidth(Double.MAX_VALUE);
                button.setMaxHeight(Double.MAX_VALUE);

                button.setOnMouseClicked(e -> {
                    handleClick(e);
                });

                button.setOnMouseEntered((event) -> {
                    if (!setHoverEffect(event)) {
                        disabledHoverEffect(event);
                    }
                });

                button.setOnMouseExited((event) -> {
                    removeHoverEffect(event);
                    noAction = false;
                });

                button.getStylesheets().add(getClass().getResource("/stylesheets/setup.css").toExternalForm());

                table.add(button, j, i);
                buttons[i][j] = button;
            }
        }

    }

    public void shipSelect(ActionEvent e) {
        File file;
        if (shipBox.getSelectionModel().getSelectedItem() == null) {
            return;
        }
        if (shipBox.getSelectionModel().getSelectedItem().equals("Boat (1 Block)")) {
            length = 1;
        } else if (shipBox.getSelectionModel().getSelectedItem().equals("Destroyer (2 Blocks)")) {
            length = 2;
        } else if (shipBox.getSelectionModel().getSelectedItem().equals("Cruiser (3 Blocks)")) {
            length = 3;
        } else if (shipBox.getSelectionModel().getSelectedItem().equals("BattleShip (4 Blocks)")) {
            length = 4;
        } else {
            length = 5;
        }
    }

    private void handleClick(MouseEvent e) {
        if (noAction) {
            return;
        }
        if (directionBox.getSelectionModel().getSelectedItem().equals("Horizontal")) {
            int start = ((Cell) e.getSource()).getColumn();
            for (int i = start; i < start + length; i++) {
                buttons[((Cell) e.getSource()).getRow()][i].setDisable(true);
                buttons[((Cell) e.getSource()).getRow()][i].setOccupied(true);
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("hovered");
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("outOfBounds");
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().add("occupied");

            }
        } else {
            int start = ((Cell) e.getSource()).getRow();
            for (int i = start; i < start + length; i++) {
                buttons[i][((Cell) e.getSource()).getColumn()].setDisable(true);
                buttons[i][((Cell) e.getSource()).getColumn()].setOccupied(true);
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("hovered");
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("outOfBounds");
                buttons[i][((Cell) e.getSource()).getColumn()].getStyleClass().add("occupied");

            }
        }

        shipBox.getItems().remove(shipBox.getSelectionModel().getSelectedItem());
        if (shipBox.getItems().isEmpty()) {
            shipBox.setDisable(true);
            readyButton.setDisable(false);
            length = 0;
        } else {
            shipBox.getSelectionModel().selectFirst();
            File file;
            if (shipBox.getSelectionModel().getSelectedItem().equals("Boat (1 Block)")) {
                // file = new File("images/ships/1Block.jpg");
                length = 1;
            } else if (shipBox.getSelectionModel().getSelectedItem().equals("Destroyer (2 Blocks)")) {
                // file = new File("images/ships/2Block.jpg");
                length = 2;
            } else if (shipBox.getSelectionModel().getSelectedItem().equals("Cruiser (3 Blocks)")) {
                // file = new File("images/ships/3Block.jpg");
                length = 3;
            } else if (shipBox.getSelectionModel().getSelectedItem().equals("BattleShip (4 Blocks)")) {
                // file = new File("images/ships/4Block.jpg");
                length = 4;
            } else {
                // file = new File("images/ships/5Block.jpg");
                length = 5;
            }
            // Image img = new Image(file.toURI().toString());
            // image.setImage(img);
        }

    }

    private boolean setHoverEffect(MouseEvent e) {
        if (directionBox.getSelectionModel().getSelectedItem().equals("Horizontal")) {
            int start = ((Cell) e.getSource()).getColumn();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE || buttons[((Cell) e.getSource()).getRow()][i].isOccupied()) {
                    return false;
                }
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().add("hovered");
            }
        } else {
            int start = ((Cell) e.getSource()).getRow();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE || buttons[i][((Cell) e.getSource()).getColumn()].isOccupied()) {
                    return false;
                }
                buttons[i][((Cell) e.getSource()).getColumn()].getStyleClass().add("hovered");
            }
        }
        return true;
    }

    private boolean removeHoverEffect(MouseEvent e) {
        if (directionBox.getSelectionModel().getSelectedItem().equals("Horizontal")) {
            int start = ((Cell) e.getSource()).getColumn();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE) {
                    return false;
                }
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("hovered");
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().remove("outOfBounds");
            }
        } else {
            int start = ((Cell) e.getSource()).getRow();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE) {
                    return false;
                }
                buttons[i][((Cell) e.getSource()).getColumn()].getStyleClass().remove("hovered");
                buttons[i][((Cell) e.getSource()).getColumn()].getStyleClass().remove("outOfBounds");
            }
        }
        return true;
    }

    private void disabledHoverEffect(MouseEvent e) {
        noAction = true;
        if (directionBox.getSelectionModel().getSelectedItem().equals("Horizontal")) {
            int start = ((Cell) e.getSource()).getColumn();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE) {
                    break;
                }
                buttons[((Cell) e.getSource()).getRow()][i].getStyleClass().add("outOfBounds");
            }
        } else {
            int start = ((Cell) e.getSource()).getRow();
            for (int i = start; i < start + length; i++) {
                if (i >= TABLE_SIZE) {
                    break;
                }
                buttons[i][((Cell) e.getSource()).getColumn()].getStyleClass().add("outOfBounds");
            }
        }
    }

    public void reset(ActionEvent e) {
    }

    public void ready(ActionEvent e) {
    }

    @Override
    public void run() {
        String message;
        while (!stop) {
            System.out.println("mesajj");
            message = p.getMessage();
            // System.out.println(message);
            System.out.println("123456789");
            execute(message);
        }
    }

    private void execute(String message) {
        System.out.println("123123");
        String[] result = message.split(" ");
        if (message.equals(DISCONNECTED)) {
            System.out.println("opponent disconnected");
            // TODO send online players
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    ((Stage) readyButton.getScene().getWindow()).close();
                    try {
                        AppManager.goToLobby(getClass().getResource("/views/lobby.fxml"), p);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        } else if (result[0].equals(READY)) {
            opponentReady = true;
            System.out.println("Opponent Ready");
            // TODO
        }
    }

    public void start() {
        System.out.println("Starting setup");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }

}