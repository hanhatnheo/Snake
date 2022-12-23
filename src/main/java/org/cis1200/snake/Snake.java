package org.cis1200.snake;

import java.awt.Color;
import java.awt.Graphics;

public class Snake extends GameObj {
    private Direction direction;
    private static final int SIZE = 10;
    private Color c;

    public Snake(int px, int py, int width, int height, Color c, Direction d) {
        super(px, py, SIZE, SIZE, width, height);
        this.c = c;
        this.direction = d;
    }

    public void changeColor(Color newC) {
        this.c = newC;
    }

    public Color getColor() {
        return this.c;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public void changeDirection(Direction d) {
        if (this.direction != Direction.opposite(d)) {
            this.direction = d;
        }
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(this.c);
        g.fillRect(this.getPx(), this.getPy(), this.getWidth(), this.getHeight());
    }
}
