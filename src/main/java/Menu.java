package main.java;

import java.awt.*;
import java.util.List;

/**
 * Created by federico on 14/03/17.
 */
public class Menu {

    private List<String> options;
    private String currentOption;
    private int x;
    private int y;

    public Menu() {}
    public Menu(List<String> options) {
        this.options = options;
        this.currentOption = options.get(0);
    }

    public List<String> getOptions() {
        return options;
    }

    public void setOptions(List<String> options) {
        this.options = options;
    }

    public String getCurrentOption() {
        return currentOption;
    }

    public void setCurrentOption(String currentOption) {
        this.currentOption = currentOption;
    }

    public void render(Graphics graphics) {

        x = (Game.WIDTH >>> 1);
        y = (Game.HEIGHT >>> 1) - 50;

        for(String option : options) {

            if(option.equals(getCurrentOption())) {
                graphics.setColor(Color.RED);
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            } else {
                graphics.setColor(Color.WHITE);
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            }
            x -= (graphics.getFontMetrics().stringWidth(option)) >> 1;
            graphics.drawString(option, x, y);
            x = (Game.WIDTH >>> 1);
            y += 50;
        }
    }

    public void render(Graphics graphics, Color color) {

        x = (Game.WIDTH >>> 1);
        y = (Game.HEIGHT >>> 1) - 50;

        for(String option : options) {

            if(option.equals(getCurrentOption())) {
                graphics.setColor(color);
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 50));
            } else {
                graphics.setColor(color);
                graphics.setFont(new Font("TimesRoman", Font.PLAIN, 30));
            }
            x -= (graphics.getFontMetrics().stringWidth(option)) >> 1;
            graphics.drawString(option, x, y);
            x = (Game.WIDTH >>> 1);
            y += 50;
        }
    }
}