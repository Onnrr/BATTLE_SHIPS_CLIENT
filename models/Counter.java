package models;

import javafx.application.Platform;
import javafx.scene.text.Text;

public class Counter implements Runnable {
    final String LEAVE = "leave";
    int seconds;
    Text text;
    Thread t;
    Player p;
    boolean stop;

    public Counter(int time, Text txt, Player player) {
        seconds = time;
        text = txt;
        p = player;
        stop = false;
    }

    @Override
    public void run() {
        while (seconds > 0 && !stop) {
            Platform.runLater(new Runnable() {
                @Override
                public void run() {
                    text.setText(String.valueOf(seconds));
                }
            });
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            seconds--;
            if (seconds == 0) {
                p.sendMessage(LEAVE);
            }
        }

    }

    public void stop() {
        stop = true;
    }

    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }
}
