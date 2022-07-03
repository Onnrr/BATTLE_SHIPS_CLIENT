package models;

import javafx.application.Platform;
import javafx.scene.text.Text;

public class Counter implements Runnable {
    int seconds;
    Text text;
    Thread t;

    public Counter(int time, Text txt) {
        seconds = time;
        text = txt;
    }

    @Override
    public void run() {
        while (seconds > 0) {
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
        }

    }

    public void start() {
        System.out.println("Starting listen");
        if (t == null) {
            t = new Thread(this, "listen");
            t.start();
        }
    }
}
