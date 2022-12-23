package org.cis1200.snake;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class FoodApple extends Food {
    public static final String IMG_FILE = "files/apple.jpeg";
    public static final int SIZE = 10;
    public static final int INIT_POS_X = 170;
    public static final int INIT_POS_Y = 170;
    private static BufferedImage img;
    public static final int DELAY = 5;
    private ArrayList<Snake> snake;

    public FoodApple(int courtWidth, int courtHeight, ArrayList<Snake> snake) {
        super(INIT_POS_X, INIT_POS_Y, SIZE, SIZE, courtWidth, courtHeight, DELAY, snake);
        try {
            if (img == null) {
                img = ImageIO.read(new File(IMG_FILE));
            }
        } catch (IOException e) {
            System.out.println("Internal Error:" + e.getMessage());
        }
        this.snake = snake;
    }

    public void draw(Graphics g) {
        g.drawImage(img, this.getPx(), this.getPy(), this.getWidth(), this.getHeight(), null);
    }

    @Override
    public int getDelay() {
        return DELAY;
    }

    @Override
    public void changeColor() {
        if (snake.get(0).intersects(this)) {
            for (Snake s : snake) {
                s.changeColor(Color.PINK);
            }
        }
    }
}
