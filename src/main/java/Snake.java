package main.java;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by federico on 04/03/17.
 */
public class Snake {

    private List<Dot> snakeDots;
    private int xDir;
    private int yDir;

    private boolean isMoving;
    private boolean increment;

    private final int STARTSIZE = 20;
    private final int STARTX = 250;
    private final int STARTY = 250;


    public Snake() {
        snakeDots = new ArrayList<>();
        this.xDir = 0;
        this.yDir = 0;
        this.isMoving = false;
        this.increment = false;
        this.snakeDots.add(new Dot(STARTX, STARTY));
        for(int i = 1; i < STARTSIZE; i++) {
            /* Adding all dots (parts) of the snake */
            snakeDots.add(new Dot(STARTX - i * 4, STARTY));
        }
    }

    public int getxDir() {
        return xDir;
    }

    public void setxDir(int xDir) {
        this.xDir = xDir;
    }

    public int getyDir() {
        return yDir;
    }

    public void setyDir(int yDir) {
        this.yDir = yDir;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public void setMoving(boolean moving) {
        isMoving = moving;
    }

    public boolean isIncrement() {
        return increment;
    }

    public void setIncrement(boolean increment) {
        this.increment = increment;
    }

    public int getX() { return this.snakeDots.get(0).getX(); }
    public int getY() { return this.snakeDots.get(0).getY(); }


    public void render(Graphics graphics) {

        graphics.setColor(Color.WHITE);
        for (Dot dot : snakeDots) {
            graphics.fillRect(dot.getX(), dot.getY(), dot.WIDTH, dot.HEIGHT);
        }
    }

    public void update() {

        if(isMoving) {
            Dot temp = snakeDots.get(0);
            Dot last = snakeDots.get(snakeDots.size() - 1);
            Dot newStart = new Dot(temp.getX() + xDir * 4, temp.getY() + yDir * 4);
            for (int i = snakeDots.size() - 1; i >= 1; i--) {
                snakeDots.set(i, snakeDots.get(i - 1));
            }
            snakeDots.set(0, newStart);
            if(isIncrement()) {
                snakeDots.add(last);
                setIncrement(false);
            }
        }
    }

    public boolean hasCollided() {

        int x = this.getX();
        int y = this.getY();

        for(int i = 1; i < snakeDots.size(); i++) {
            if(snakeDots.get(i).getX() == x && snakeDots.get(i).getY() == y) {
                return true;
            }
        }
        return false;
    }
}
