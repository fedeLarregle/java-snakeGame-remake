package main.java;

import java.awt.*;

/**
 * Created by federico on 05/03/17.
 */
public class Food {

    private int x;
    private int y;

    private Snake snake;
    private int WINDOWWIDTH = Game.WIDTH;
    private int WINDOWHEIGHT = Game.HEIGHT;

    private int width = 6;
    private int height = 6;

    /* Number of pixels that the food exceeds from the snake */
    private int widthOffset = 2;
    private int heightOffset = 2;

    public Food(Snake snake) {
        this.x = (int)(Math.random() * WINDOWWIDTH - width);
        this.y = (int) (Math.random() * WINDOWHEIGHT - height);
        this.snake = snake;
    }

    private void setNewPosition() {
        this.x = (int)(Math.random() * WINDOWWIDTH - width);
        this.y = (int) (Math.random() * WINDOWHEIGHT - height);
    }

    public boolean hasCollidedWithSnake() {
        int snakeX = snake.getX() + widthOffset;
        int snakeY = snake.getY() + heightOffset;

        if(snakeX >= this.x && snakeX <= (this.x + width)) {
            if(snakeY >= this.y && snakeY <= (this.y + height)) {
                setNewPosition();
                snake.setIncrement(true);
                snake.setScore(100);
                return true;
            }
        }
        return false;
    }


    public void render(Graphics graphics) {
        graphics.setColor(Color.WHITE);
        graphics.fillRect(this.x, this.y, width, height);
    }
}
