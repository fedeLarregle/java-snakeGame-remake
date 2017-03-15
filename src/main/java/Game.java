package main.java;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.Arrays;

/**
 * Created by federico on 04/03/17.
 */
public class Game extends Canvas implements Runnable, KeyListener {

    /* WIDTH and HEIGHT of the window */
    public static int WIDTH = 500;
    public static int HEIGHT = 500;

    private boolean isRunning = false;

    private Thread thread;
    private JFrame frame;

    private int FPS = 30;

    private Menu menu;
    private GameState gameState;

    private BufferedImage image;

    private Snake snake;
    private Food food;




    public Game() {

        Dimension size = new Dimension(WIDTH, HEIGHT);
        setPreferredSize(size);
        setFocusable(true);
        requestFocus();

        frame = new JFrame();
        snake = new Snake();
        food = new Food(snake);

        menu = new Menu(Arrays.asList("Game ON", "QUIT"));
        gameState = GameState.SELECCION;

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

    public enum GameState {
        PAUSED,
        SELECCION,
        GAME_STATE,
        GAME_OVER
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
            if(getGameState().equals(GameState.GAME_STATE)) { this.checkGameOver(); }
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

        if(getGameState().equals(GameState.GAME_STATE)) {
            snake.update();
            food.hasCollidedWithSnake();
        }

    }

    public void render() {

        BufferStrategy bs = getBufferStrategy();

        if (bs == null) {
            createBufferStrategy(3);
            return;
        }

        Graphics graphics = bs.getDrawGraphics();

        // Painting the background with BLACK color
        graphics.setColor(Color.BLACK);
        graphics.fillRect(0, 0, WIDTH, HEIGHT);

        // If we are in the SELECCION mode we render the default menu
        if (getGameState().equals(GameState.SELECCION)) {
            menu.render(graphics);
        } else if (getGameState().equals(GameState.GAME_STATE)) {

            // If we are in the GAME STATE we are going to render the snake, score and food
            snake.render(graphics);
            food.render(graphics);

            graphics.setColor(Color.WHITE);
            graphics.drawString("Player Score: " + String.valueOf(snake.getScore()),
                    WIDTH - (WIDTH >>> 2), (HEIGHT >>> 4));

        } else if (getGameState().equals(GameState.PAUSED)) {
            // If we are in the PAUSED state we are going to render the paused state
            menu.setOptions(Arrays.asList("RESUME"));
            menu.render(graphics);
        } else if (getGameState().equals(GameState.GAME_OVER)) {
            // If we are in the GAME OVER state we are going to render the GAME OVER state with the Final Score
            menu.setOptions(Arrays.asList("GAME OVER (PRESS SCAPE)",
                    "Final Score: " + String.valueOf(snake.getScore())));
            menu.render(graphics, Color.RED);
        }

        graphics.drawImage(image, 0, 0, WIDTH, HEIGHT, null);

        graphics.dispose();
        bs.show();

    }

    public void checkGameOver() {

        /*
        * Checking for collision with the borders of the window and with the snake it self
        */
        if(snake.getX() < 0 || snake.getX() > WIDTH - 4) { setGameState(GameState.GAME_OVER); }
        if(snake.getY() < 0 || snake.getY() > HEIGHT - 4) { setGameState(GameState.GAME_OVER); }
        if(snake.hasCollided()) { setGameState(GameState.GAME_OVER); }

    }

    public void setGameState(Game.GameState gameState) { this.gameState = gameState; }
    public GameState getGameState() { return this.gameState; }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {

        /*
        * Setting the listeners for the Key events of DOWN and UP if we are in the SELECCION MENU
        */
        if(getGameState().equals(GameState.SELECCION)){

            if (menu.getCurrentOption().equals(menu.getOptions().get(0))) {

                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    setGameState(GameState.GAME_STATE);
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    menu.setCurrentOption("QUIT");
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    menu.setCurrentOption("QUIT");
                }
            } else if (menu.getCurrentOption().equals(menu.getOptions().get(1))) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
                }

                if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    menu.setCurrentOption("Game ON");
                } else if (e.getKeyCode() == KeyEvent.VK_UP) {
                    menu.setCurrentOption("Game ON");
                }
            }
        } else if (getGameState().equals(GameState.GAME_STATE)) {

            // Checking if the player PAUSED the game (with P key)
            if(e.getKeyCode() == KeyEvent.VK_P) {
                setGameState(GameState.PAUSED);
            }

            // We are going to setMoving for the first time only if the player moves to UP, DOWN or RIGHT
            if (!snake.isMoving()) {
                if (e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_DOWN
                        || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    snake.setMoving(true);
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (snake.getyDir() != 1) {
                    snake.setyDir(-1);
                    snake.setxDir(0);
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (snake.getyDir() != -1) {
                    snake.setyDir(1);
                    snake.setxDir(0);
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                if (snake.getxDir() != 1) {
                    snake.setxDir(-1);
                    snake.setyDir(0);
                }
            }

            if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
                if (snake.getxDir() != -1) {
                    snake.setxDir(1);
                    snake.setyDir(0);
                }
            }
        } else if (getGameState().equals(GameState.PAUSED)) {
            if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                setGameState(GameState.GAME_STATE);
            }

        } else if (getGameState().equals(GameState.GAME_OVER)) {
            if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                this.frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
            }
        }
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
