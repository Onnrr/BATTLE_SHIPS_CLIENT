package models;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import javafx.scene.image.Image;

public class Player {
    final int TABLE_SIZE = 10;
    final int SHIPS = 20;

    int id;
    int opponentId;
    String opponentName;
    Socket s;
    String userName;
    String mail;
    Image photo;
    int score;
    String onlinePlayers;
    String rank;

    BufferedReader in;
    PrintWriter out;

    int[][] myTable;

    int remaining;

    public Player(Socket socket, BufferedReader in, PrintWriter out, int id, String userName, int score, String mail,
            Image img) {
        s = socket;
        this.id = id;
        this.userName = userName;
        this.mail = mail;
        photo = img;
        this.score = score;
        onlinePlayers = "";

        this.in = in;
        this.out = out;
        opponentId = -1;
        opponentName = "";

        remaining = 20;
        myTable = new int[TABLE_SIZE][TABLE_SIZE];
    }

    public void setOnlinePlayers(String online) {
        onlinePlayers = online;
    }

    public void settOpponentID(int opId) {
        opponentId = opId;
    }

    public int getOpponentID() {
        return opponentId;
    }

    public void setOpponentName(String opName) {
        opponentName = opName;
    }

    public String getOpponentName() {
        return opponentName;
    }

    public String getOnlinePlayers() {
        return onlinePlayers;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getRank() {
        return rank;
    }

    public Socket getSocket() {
        return s;
    }

    public String getName() {
        return userName;
    }

    public int getID() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public String getMail() {
        return mail;
    }

    public String getMessage() {
        try {
            return in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

    public void sendMessage(String message) {
        out.println(message);
    }

    public int[][] getMyTable() {
        return myTable;
    }

    public void resetTable() {
        myTable = new int[TABLE_SIZE][TABLE_SIZE];
    }
}
