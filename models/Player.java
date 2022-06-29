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
    Socket s;
    String userName;
    String mail;
    Image photo;
    int score;
    String onlinePlayers;

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

        remaining = 20;
        myTable = null;
    }

    public void setOnlinePlayers(String online) {
        onlinePlayers = online;
    }

    public String getOnlinePlayers() {
        return onlinePlayers;
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
}
