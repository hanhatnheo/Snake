package org.cis1200.snake;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FoodEgg extends Food {
    public static final String IMG_FILE = "files/egg.png";
    public static final int SIZE = 10;
    public static final int INIT_POS_X = 130;
    public static final int INIT_POS_Y = 130;
    private static BufferedImage img;
    private static int delay = 3;
    private ArrayList<Snake> snake;

    public FoodEgg(int courtWidth, int courtHeight, ArrayList<Snake> snake) {
        super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight, delay, snake);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        this.snake = snake;
    }
    @Override
    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public int getDelay() {
        return delay * (-1);
    }

    @Override
    public void changeColor() {
        if (snake.get(0).intersects(this)) {
            for (Snake s : snake) {
                s.changeColor(Color.YELLOW);
            }
        }
    }
}
