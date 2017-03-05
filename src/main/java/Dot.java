package main.java;

/**
 * Created by federico on 04/03/17.
 */
public class Dot {

    public static int WIDTH = 4;
    public static int HEIGHT = 4;

    private int x;
    private int y;


    public Dot() {
        this.x = 0;
        this.y = 0;
    }
    public Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }
}
