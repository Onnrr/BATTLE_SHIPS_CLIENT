package models;

import javafx.application.Platform;
import javafx.scene.text.Text;

/**
 * Simple down counter for integers up to zero
 * Decreases 1 at each second
 */
public class Counter implements Runnable {
    final String LEAVE = "leave";
    final String SKIP = "skip";
    String message;
    int seconds;
    int maxTime;
    Text text;
    Text turn;
    Thread t;
    Player p;
    boolean stop;

    /**
     * Creates the counter for setup part
     * 
     * @param time   The starting value of the counter
     * @param txt    javafx text to display the counter
     * @param player to send messages to server according to the counter values
     */
    public Counter(int time, Text txt, Player player) {
        seconds = time;
        text = txt;
        p = player;
        stop = false;
        turn = null;
        message = LEAVE;
        maxTime = time;
    }

    /**
     * Creates the counter for game part
     * 
     * @param time   The starting value of the counter
     * @param txt    javafx text to display the counter
     * @param player to send messages to server according to the counter values
     * @param turn   text to set turn value
     */
    public Counter(int time, Text count, Text turn, Player player) {
        seconds = time;
        text = count;
        p = player;
        stop = false;
        this.turn = turn;
        message = SKIP;
        maxTime = time;
    }

    @Override
    /**
     * Thread to get messages from the server
     */
    public void run() {
        System.out.println("stop = " + stop);
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
                if (turn != null) {
                    turn.setText(p.getOpponentName() + "'s turn");
                    p.setMyTurn(false);
                    stop();
                    reset();
                    text.setVisible(false);
                }
                p.sendMessage(message);
            }
        }

    }

    /**
     * Resets the counter
     */
    public void reset() {
        System.out.println("counter reset");
        seconds = maxTime;
    }

    /**
     * Stops the counter
     */
    public void stop() {
        stop = true;
    }

    /**
     * Starts the thread
     */
    public void start() {
        stop = false;
        System.out.println("Starting count");
        if (t == null) {
            t = new Thread(this, "count");
            t.start();
        }
    }
}
