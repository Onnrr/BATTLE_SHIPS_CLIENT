package models;

import java.net.Socket;

import javafx.scene.image.Image;

public class Player {
    final int TABLE_SIZE = 10;
    final int SHIPS = 20;

    int id;
    Socket s;
    String userName;
    String mail;
    Image photo;
    int score;

    int[][] myTable;

    int remaining;

    public Player(Socket socket, int id, String userName, int score, String mail, Image img) {
        s = socket;
        this.id = id;
        this.userName = userName;
        this.mail = mail;
        photo = img;
        this.score = score;

        remaining = 20;
        myTable = null;
    }

    public Socket getSocket() {
        return s;
    }

    public String getName() {
        return userName;
    }
}
