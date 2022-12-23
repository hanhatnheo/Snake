package org.cis1200.snake;

import java.util.ArrayList;

public abstract class Food extends GameObj {
    private int delay;
    private int width;
    private int height;
    private ArrayList<Snake> s;
    public Food(int posX, int posY, int width, int height, int courtWidth, int courtHeight,
                int delay, ArrayList<Snake> s) {
        super(posX, posY, width, height, courtWidth, courtHeight);
        this.delay = delay;
        this.s = s;
        this.width = courtWidth;
        this.height = courtHeight;
    }

    public void allocateFood() {
        boolean flag = true;
        while (flag) {
            int x = (int) (width * Math.random());
            int y = (int) (height * Math.random());
            for (Snake snake : s) {
                if (snake.getPx() == x && snake.getPy() == y) {
                    flag = true;
                    break;
                } else {
                    setPx(x);
                    setPy(y);
                    flag = false;
                }
            }
        }
    }

    public abstract int getDelay();
    public abstract void changeColor();


}

