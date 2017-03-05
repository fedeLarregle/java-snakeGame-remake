package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;

/**
 * Created by federico on 04/03/17.
 */
public class Game extends Canvas implements Runnable, KeyListener {

    /* WIDTH and HEIGHT of the window */
    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    private boolean isRunning = false;
    private boolean gameOver = false;

    private Thread thread;
    private JFrame frame;

    private int FPS = 30;

    private BufferedImage image;



    public Game() {

        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setFocusable(true);
        requestFocus();

        frame = new JFrame();

        addKeyListener(this);
    }

    public synchronized void start() {
        isRunning = true;
        thread = new Thread(this);
        thread.start();
    }

    public synchronized void stop() {
        isRunning = false;
        try {
            /* Waiting for the thread of the game to stop */
            thread.join();
        } catch (InterruptedException ie) {
            ie.printStackTrace();
        }
    }


    @Override
    public void run() {

        long startTime;
        long waitTime;
        long URTimeMillis;
        long targetTime = 1_000 / FPS; // Time that should take per frame



        /* GAME LOOP */
        while(isRunning) {

            startTime = System.nanoTime();
            update();
            render();
            URTimeMillis = (System.nanoTime() - startTime) / 1_000_000;

            waitTime = targetTime - URTimeMillis; // Checking if took longer than expected

            if(waitTime > 0) {
                try {
                    thread.sleep(waitTime);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }


        }
    }

    public void update() {

    }

    public void render() {

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

    }

    @Override
    public void keyReleased(KeyEvent e) {

    }



    public static void main(String... args) {

        Game game = new Game();
        game.frame.setResizable(false);
        game.frame.setTitle("Snake Remake");
        game.frame.add(game);

        game.frame.pack();
        game.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.frame.setLocationRelativeTo(null);
        game.frame.setVisible(true);

        game.start();
    }


}